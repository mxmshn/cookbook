package ru.mashnin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mashnin.dto.request.RegistrationUserDto;
import ru.mashnin.dto.response.UserResponseDto;
import ru.mashnin.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> startRegister(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        authService.startRegistration(registrationUserDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<?> completeRegister(@RequestParam("token") String token) {
        UserResponseDto userResponseDto = authService.completeRegistration(token);
        return ResponseEntity.status(201).body(String.format("Email '%s' активирован", userResponseDto.getEmail()));
    }
}
