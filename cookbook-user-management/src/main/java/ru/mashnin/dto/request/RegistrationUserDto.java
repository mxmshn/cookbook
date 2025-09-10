package ru.mashnin.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationUserDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    @AssertTrue(message = "Пароли не совпадают")
    public boolean isPasswordEquals() {
        return password.equals(confirmPassword);
    }
}
