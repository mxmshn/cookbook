package ru.mashnin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mashnin.dto.request.LoginRequest;
import ru.mashnin.dto.request.RegistrationRequest;
import ru.mashnin.dto.response.LoginResponse;
import ru.mashnin.service.AuthService;
import ru.mashnin.service.LoginService;
import ru.mashnin.service.RegistrationService;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final RegistrationService registrationService;
    private final LoginService loginService;

    @Override
    public void beginRegistration(RegistrationRequest userDto) {
        registrationService.beginRegistration(userDto);
    }

    @Override
    public void completeRegistration(String confirmationToken) {
        registrationService.completeRegistration(confirmationToken);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }
}
