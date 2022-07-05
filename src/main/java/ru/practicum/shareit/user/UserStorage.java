package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    List<User> findAllUsers();
    User findUserById(Long id);
    User addUser(User user);
    User updateUser(User newUser);
    void deleteUserById(Long id);
}
