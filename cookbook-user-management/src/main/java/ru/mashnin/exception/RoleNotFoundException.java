package ru.mashnin.exception;

import ru.mashnin.enums.RoleName;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(RoleName roleName) {
        super("Роль не найдена: " + roleName);
    }
}