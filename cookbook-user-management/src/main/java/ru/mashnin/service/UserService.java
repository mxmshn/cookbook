package ru.mashnin.service;

import ru.mashnin.dto.request.RegistrationUserDto;
import ru.mashnin.dto.response.UserResponseDto;

public interface UserService {
    UserResponseDto register(RegistrationUserDto registrationUserDto);
}
