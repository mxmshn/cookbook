package ru.mashnin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mashnin.entity.Role;
import ru.mashnin.enums.RoleName;
import ru.mashnin.exception.RoleNotFoundException;
import ru.mashnin.repository.RoleRepository;
import ru.mashnin.service.RoleService;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByName(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> {
                    log.warn("Роль '{}' не найдена в базе", roleName);
                    return new RoleNotFoundException(roleName);
                });
    }
}
