package ru.mashnin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mashnin.dto.RegistrationData;
import ru.mashnin.dto.request.LoginRequest;
import ru.mashnin.dto.request.RegistrationRequest;
import ru.mashnin.dto.response.LoginResponse;
import ru.mashnin.dto.response.UserResponseDto;
import ru.mashnin.entity.User;
import ru.mashnin.enums.RoleName;
import ru.mashnin.exception.InvalidCredentialsException;
import ru.mashnin.exception.UserAlreadyExistsException;
import ru.mashnin.factory.EmailContent;
import ru.mashnin.factory.EmailContentFactory;
import ru.mashnin.mapper.UserMapper;
import ru.mashnin.service.*;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private static final int REGISTRATION_EXPIRATION_HOURS = 24;
    private static final TimeUnit REGISTRATION_EXPIRATION_UNIT = TimeUnit.HOURS;

    private final UserService userService;
    private final RoleService roleService;
    private final RedisService redisService;
    private final MailService mailService;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailContentFactory emailContentFactory;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public void beginRegistration(RegistrationRequest userDto) {
        if (userService.existsUserByEmail(userDto.getEmail())) {
            log.warn("Попытка регистрации пользователя с уже существующим email: {}", userDto.getEmail());
            throw new UserAlreadyExistsException(userDto.getEmail());
        }

        String confirmationToken = UUID.randomUUID().toString();
        String registrationRedisKey = getRegistrationDataKey(confirmationToken);

        RegistrationData registrationData = new RegistrationData(
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword())
        );

        redisService.save(registrationRedisKey, registrationData,
                REGISTRATION_EXPIRATION_HOURS,
                REGISTRATION_EXPIRATION_UNIT);

        String confirmationLink = baseUrl + "/confirm-email?confirmationToken=" + confirmationToken;
        EmailContent emailContent = emailContentFactory.createConfirmationEmail(userDto.getEmail(), confirmationLink);
        mailService.sendMessage(emailContent);

    }

    public UserResponseDto completeRegistration(String confirmationToken) {
        String registrationDataKey = getRegistrationDataKey(confirmationToken);
        RegistrationData registrationData = redisService.get(registrationDataKey, RegistrationData.class);

        if (registrationData == null) {
            log.warn("Попытка завершения регистрации с недействительным токеном: {}", confirmationToken);
            throw new IllegalArgumentException("Недействительный или просроченный токен");
        }

        User user = new User();
        user.setEmail(registrationData.getEmail());
        user.setPasswordHash(registrationData.getPasswordHash());
        user.setRoles(Set.of(roleService.getRoleByName(RoleName.USER)));

        User savedUser = userService.createUser(user);

        redisService.delete(registrationDataKey);

        EmailContent emailContent = emailContentFactory.createWelcomeEmail(user.getEmail(), user.getEmail());
        mailService.sendMessage(emailContent);

        log.info("Успешная регистрация пользователя: {}", user.getEmail());
        return userMapper.toResponseDto(savedUser);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            log.warn("Неверные учетные данные для пользователя: {}", loginRequest.getUsername());
            throw new InvalidCredentialsException();
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        log.info("Успешная аутентификация пользователя: {}", loginRequest.getUsername());
        return new LoginResponse(accessToken, refreshToken);
    }

    private String getRegistrationDataKey(String token) {
        return "registration_data:" + token;
    }
}
