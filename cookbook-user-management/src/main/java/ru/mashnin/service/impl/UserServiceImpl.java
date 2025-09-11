package ru.mashnin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mashnin.entity.User;
import ru.mashnin.repository.UserRepository;
import ru.mashnin.service.UserService;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        log.info("В базе данных создан новый пользователь: ID={}, email={}", savedUser.getId(), savedUser.getEmail());
        return savedUser;
    }

    public boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

}
