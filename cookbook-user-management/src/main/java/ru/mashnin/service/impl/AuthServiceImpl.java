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
import ru.mashnin.mapper.UserMapper;
import ru.mashnin.service.*;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RoleService roleService;
    private final RedisService redisService;
    private final MailService mailService;
    private final SecurityUserService securityUserService;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public void startRegistration(RegistrationRequest userDto) {
        if (userService.existsUserByEmail(userDto.getEmail())) {
            log.warn("Попытка регистрации пользователя с уже существующим email: {}", userDto.getEmail());
            throw new UserAlreadyExistsException(userDto.getEmail());
        }

        String token = UUID.randomUUID().toString();
        String registrationDataKey = "registration_data:" + token;

        RegistrationData registrationData = new RegistrationData(
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword())
        );

        redisService.save(registrationDataKey, registrationData, 24, TimeUnit.HOURS);
        String confirmationLink = baseUrl + "/confirm-email?token=" + token;
        mailService.sendEmailConfirmation(userDto.getEmail(), confirmationLink);

    }

    public UserResponseDto completeRegistration(String token) {
        String registrationDataKey = "registration_data:" + token;
        RegistrationData registrationData = redisService.get(registrationDataKey, RegistrationData.class);

        if (registrationData == null) {
            throw new IllegalArgumentException("Недействительный или просроченный токен");
        }

        User user = new User();
        user.setEmail(registrationData.getEmail());
        user.setPasswordHash(registrationData.getPasswordHash());
        user.setRoles(Set.of(roleService.getRoleByName(RoleName.USER)));

        User savedUser = userService.createUser(user);

        redisService.delete(registrationDataKey);

        mailService.sendWelcomeEmail(user.getEmail(), user.getEmail());
        return userMapper.toResponseDto(savedUser);
    }

    public LoginResponse createAuthToken(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            log.warn("Неверный логин или пароль");
            throw new InvalidCredentialsException();
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return new LoginResponse(accessToken, refreshToken);
    }
}
