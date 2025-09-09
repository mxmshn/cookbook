package ru.mashnin.service;

import ru.mashnin.entity.Role;
import ru.mashnin.enums.RoleName;

public interface RoleService {
    Role getRoleByName(RoleName roleName);
}
