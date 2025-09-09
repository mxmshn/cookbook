package ru.mashnin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mashnin.dto.request.RegistrationUserDto;
import ru.mashnin.dto.response.UserResponseDto;
import ru.mashnin.entity.Role;
import ru.mashnin.entity.User;
import ru.mashnin.enums.RoleName;
import ru.mashnin.repository.UserRepository;
import ru.mashnin.service.RoleService;
import ru.mashnin.service.UserService;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(RegistrationUserDto userDto) {
        if(!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            log.info("Пароли не совпадают");
            throw new RuntimeException("пароли не совпадают");
        }
        if(userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            log.info("Пользователь с данным email уже существует");
            throw new RuntimeException("Пользователь уже существует");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Set.of(roleService.getRoleByName(RoleName.USER)));
        user.setEmail(userDto.getEmail());
        userRepository.save(user);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setCreatedAt(user.getCreatedAt());
        userResponseDto.setRoles(user.getRoles());
        return userResponseDto;
    }
}
