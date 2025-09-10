package ru.mashnin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mashnin.dto.request.RegistrationUserDto;
import ru.mashnin.dto.response.UserResponseDto;
import ru.mashnin.entity.User;
import ru.mashnin.enums.RoleName;
import ru.mashnin.exception.UserAlreadyExistsException;
import ru.mashnin.mapper.UserMapper;
import ru.mashnin.repository.UserRepository;
import ru.mashnin.service.RoleService;
import ru.mashnin.service.UserService;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto register(RegistrationUserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            log.warn("Попытка регистрации с уже существующим email: {}", userDto.getEmail());
            throw new UserAlreadyExistsException(userDto.getEmail());
        }

        User user = userMapper.toEntity(userDto);
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Set.of(roleService.getRoleByName(RoleName.USER)));

        User saved = userRepository.save(user);

        log.info("Создан новый пользователь: ID={}, email={}", saved.getId(), saved.getEmail());
        return userMapper.toResponseDto(saved);
    }
}
