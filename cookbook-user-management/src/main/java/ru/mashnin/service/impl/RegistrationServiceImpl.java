package ru.mashnin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mashnin.dto.RegistrationData;
import ru.mashnin.dto.request.RegistrationRequest;
import ru.mashnin.entity.User;
import ru.mashnin.enums.RoleName;
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
public class RegistrationServiceImpl implements RegistrationService {

    private static final int REGISTRATION_EXPIRATION_HOURS = 24;
    private static final TimeUnit REGISTRATION_EXPIRATION_UNIT = TimeUnit.HOURS;

    private final UserService userService;
    private final RoleService roleService;
    private final RedisService redisService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final EmailContentFactory emailContentFactory;
    private final UserMapper userMapper;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    @Transactional
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

    @Override
    @Transactional
    public void completeRegistration(String confirmationToken) {

        String registrationDataKey = getRegistrationDataKey(confirmationToken);
        RegistrationData registrationData = redisService.get(registrationDataKey, RegistrationData.class);

        if (registrationData == null) {
            log.warn("Попытка завершения регистрации с недействительным токеном: {}", confirmationToken);
            throw new IllegalArgumentException("Недействительный или просроченный токен");
        }

        User user = userMapper.toEntity(registrationData);
        user.setRoles(Set.of(roleService.getRoleByName(RoleName.USER)));
        userService.createUser(user);

        redisService.delete(registrationDataKey);

        EmailContent emailContent = emailContentFactory.createWelcomeEmail(user.getEmail(), user.getEmail());
        mailService.sendMessage(emailContent);

        log.info("Успешная регистрация пользователя: {}", user.getEmail());
    }

    private String getRegistrationDataKey(String token) {
        return "registration_data:" + token;
    }
}
