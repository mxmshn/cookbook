package ru.mashnin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mashnin.dto.request.LoginRequest;
import ru.mashnin.dto.response.LoginResponse;
import ru.mashnin.exception.InvalidCredentialsException;
import ru.mashnin.service.JwtService;
import ru.mashnin.service.LoginService;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            log.warn("Неверные учетные данные для пользователя: {}", loginRequest.getUsername());
            throw new InvalidCredentialsException();
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        log.info("Успешная аутентификация пользователя: {}", loginRequest.getUsername());
        return new LoginResponse(accessToken, refreshToken);
    }
}
