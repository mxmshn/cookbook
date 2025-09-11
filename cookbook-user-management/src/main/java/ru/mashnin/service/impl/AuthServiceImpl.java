package ru.mashnin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mashnin.dto.RegistrationData;
import ru.mashnin.dto.request.RegistrationUserDto;
import ru.mashnin.dto.response.UserResponseDto;
import ru.mashnin.entity.User;
import ru.mashnin.enums.RoleName;
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
    private final UserMapper userMapper;
    private final RedisService redisService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void startRegistration(RegistrationUserDto userDto) {
        if (userService.existsUserByEmail(userDto.getEmail())) {
            log.warn("Попытка регистрации пользователя с уже существующим email: {}", userDto.getEmail());
            throw new UserAlreadyExistsException(userDto.getEmail());
        }

        String token = UUID.randomUUID().toString();
        String registrationDataKey = "registration_data:" + token;

        // Сохраняем данные регистрации в Redis
        RegistrationData registrationData = new RegistrationData(
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword())
        );

        redisService.save(registrationDataKey, registrationData, 24, TimeUnit.HOURS);
        String confirmationLink = "http://localhost:8080" + "/confirm-email?token=" + token;
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

        // Очищаем Redis
        redisService.delete(registrationDataKey);
        redisService.delete("pending_registration:" + registrationData.getEmail());

        mailService.sendWelcomeEmail(user.getEmail(), user.getEmail());
        return userMapper.toResponseDto(savedUser);

    }
}
