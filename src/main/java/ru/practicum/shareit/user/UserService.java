package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User addUser(User user);
    User updateUser(Long id, User newUser);
    void deleteUserById(Long id);
}
