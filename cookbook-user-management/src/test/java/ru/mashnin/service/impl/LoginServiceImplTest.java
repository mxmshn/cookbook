package ru.mashnin.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import ru.mashnin.dto.request.LoginRequest;
import ru.mashnin.dto.response.LoginResponse;
import ru.mashnin.exception.InvalidCredentialsException;
import ru.mashnin.service.JwtService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Mock
    public AuthenticationManager manager;

    @Mock
    public UserDetails userDetails;

    @Mock
    public JwtService jwtService;

    @Mock
    public Authentication authentication;

    @InjectMocks
    public LoginServiceImpl loginService;

    @Test
    public void login_SuccessfulAuthentication_ReturnsLoginResponse() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");

        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateAccessToken(userDetails)).thenReturn("access");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("refresh");

        LoginResponse loginResponse = loginService.login(loginRequest);

        Mockito.verify(manager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertEquals("access", loginResponse.getAccessToken());
        assertEquals("refresh", loginResponse.getRefreshToken());
    }

    @Test
    public void login_InvalidCredentials_ThrowsException() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("wrongPassword");

        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Bad cred"));

        assertThrows(InvalidCredentialsException.class, () -> {loginService.login(loginRequest);});

    }
}