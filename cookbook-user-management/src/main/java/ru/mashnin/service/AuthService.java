package ru.mashnin.service;

import ru.mashnin.dto.request.LoginRequest;
import ru.mashnin.dto.request.RegistrationRequest;
import ru.mashnin.dto.response.LoginResponse;
import ru.mashnin.dto.response.UserResponseDto;

public interface AuthService {
    void beginRegistration(RegistrationRequest registrationRequest);
    void completeRegistration(String token);
    LoginResponse login(LoginRequest loginRequest);
}
