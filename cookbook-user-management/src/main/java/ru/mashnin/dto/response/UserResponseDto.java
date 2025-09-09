package ru.mashnin.dto.response;

import lombok.Data;
import ru.mashnin.entity.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID id;
    private String email;
    private LocalDateTime createdAt;
    private Set<Role> roles;
}
