package ru.mashnin.dto.request;

import lombok.Data;

@Data
public class RegistrationUserDto {
    private String email;
    private String password;
    private String confirmPassword;
}
