package ru.mashnin.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JwtService {
    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String getUsername(String token);

    List<String> getRoles(String token);

    String refreshAccessToken(String token);

    void validateToken(String token);
}