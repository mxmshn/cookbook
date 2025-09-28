package ru.mashnin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mashnin.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsUserByEmail(String email);
}
