package ru.mashnin.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.mashnin.config.JwtProperties;
import ru.mashnin.service.JwtService;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final JwtProperties properties;

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, Duration.ofMinutes(properties.getAccessLifetimeMinutes()));
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, Duration.ofDays(properties.getRefreshLifetimeDays()));
    }


    private String generateToken(UserDetails userDetails, Duration jwtLifetime) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", rolesList);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        return (List<String>) getAllClaimsFromToken(token).get("roles");
    }

    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token);
        } catch (SignatureException e) {
            log.warn("Неверная подпись JWT");
            throw e;
        } catch (ExpiredJwtException e) {
            log.warn("Срок действия JWT истек");
            throw e;
        } catch (Exception e) {
            log.warn("Некорректный JWT");
            throw e;
        }
    }

    public String refreshAccessToken(String token) {
        validateToken(token);
        List<SimpleGrantedAuthority> list = getRoles(token).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails userDetails = new User(getUsername(token),
                "",
                list);
        return generateAccessToken(userDetails);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
