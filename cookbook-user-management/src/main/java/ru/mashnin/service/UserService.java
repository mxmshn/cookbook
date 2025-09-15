package ru.mashnin.service;


import ru.mashnin.entity.User;

public interface UserService {
    User createUser(User user);
    boolean existsUserByEmail(String email);
}
