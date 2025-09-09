package ru.mashnin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mashnin.entity.Role;
import ru.mashnin.enums.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Short> {
    Optional<Role> findByName(RoleName name);
}
