package ru.mashnin.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final UUID id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UUID id, String username, String password,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities != null ? authorities : Collections.emptyList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
