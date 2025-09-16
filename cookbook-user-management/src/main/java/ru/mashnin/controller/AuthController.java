package ru.mashnin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mashnin.dto.request.LoginRequest;
import ru.mashnin.dto.request.RegistrationRequest;
import ru.mashnin.dto.response.ApiResponse;
import ru.mashnin.dto.response.LoginResponse;
import ru.mashnin.dto.response.UserResponseDto;
import ru.mashnin.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        authService.beginRegistration(registrationRequest);
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Регистрация начата, подвердите email",
                null));
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<ApiResponse<UserResponseDto>> confirmEmail(@RequestParam("confirmationToken") String token) {
        UserResponseDto userResponseDto = authService.completeRegistration(token);
        return ResponseEntity.status(201).body(new ApiResponse<>(true,
                String.format("Email '%s' активирован", userResponseDto.getEmail()),
                userResponseDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Успешная аутентификация",
                authService.login(loginRequest)));
    }
}
