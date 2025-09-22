package ru.mashnin.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mashnin.dto.request.LoginRequest;
import ru.mashnin.dto.request.RegistrationRequest;
import ru.mashnin.dto.response.AccessTokenResponse;
import ru.mashnin.dto.response.ApiResponse;
import ru.mashnin.dto.response.LoginResponse;
import ru.mashnin.service.JwtService;
import ru.mashnin.service.LoginService;
import ru.mashnin.service.RegistrationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RegistrationService registrationService;
    private final LoginService loginService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        registrationService.beginRegistration(registrationRequest);
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Регистрация начата, подвердите email",
                null));
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<ApiResponse<Void>> confirmEmail(@RequestParam("confirmationToken") String token) {
        registrationService.completeRegistration(token);
        return ResponseEntity.status(201).body(new ApiResponse<>(true,
                "Email успешно активирован",
                null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse loginResponse = loginService.login(loginRequest);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", loginResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        AccessTokenResponse responseBody = new AccessTokenResponse(loginResponse.getAccessToken());

        return ResponseEntity.ok(new ApiResponse<>(true,
                "Успешная аутентификация",
                responseBody));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(@CookieValue("refreshToken") String refreshToken) {
        String accessToken = jwtService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Новый access токен",
                accessToken));
    }
}
