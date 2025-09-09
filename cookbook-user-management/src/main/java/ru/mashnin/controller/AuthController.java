package ru.mashnin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mashnin.dto.request.RegistrationUserDto;
import ru.mashnin.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationUserDto registrationUserDto) {
        return ResponseEntity.ok(userService.register(registrationUserDto));
    }
}
