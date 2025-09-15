package ru.mashnin.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mashnin.dto.RegistrationData;
import ru.mashnin.dto.request.RegistrationRequest;
import ru.mashnin.service.MailService;
import ru.mashnin.service.RedisService;
import ru.mashnin.service.UserService;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private RedisService redisService;

    @Mock
    private MailService mailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void startRegistration_WithNewEmail_ShouldSaveToRedisAndSendEmail() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        when(userService.existsUserByEmail(request.getEmail())).thenReturn(false);

        authService.startRegistration(request);


        verify(userService).existsUserByEmail(request.getEmail());
        verify(redisService).save(anyString(), any(RegistrationData.class), eq(24L), eq(TimeUnit.HOURS));
        verify(mailService).sendEmailConfirmation(eq(request.getEmail()), anyString());
    }

}