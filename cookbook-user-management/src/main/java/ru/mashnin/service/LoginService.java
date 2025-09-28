package ru.mashnin.service;

import ru.mashnin.dto.request.LoginRequest;
import ru.mashnin.dto.response.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest loginRequest);
}
