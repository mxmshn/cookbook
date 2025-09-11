package ru.mashnin.service;

import ru.mashnin.dto.request.RegistrationUserDto;
import ru.mashnin.dto.response.UserResponseDto;

public interface AuthService {
    void startRegistration(RegistrationUserDto registrationUserDto);
    UserResponseDto completeRegistration(String token);
}
