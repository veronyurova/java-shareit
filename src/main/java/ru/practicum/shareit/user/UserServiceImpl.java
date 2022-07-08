package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Service
@Validated
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.findAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        return userStorage.findUserById(id);
    }

    @Override
    public User addUser(@Valid User user) {
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(Long id, User newUser) {
        return userStorage.updateUser(id, newUser);
    }

    @Override
    public void deleteUserById(Long id) {
        userStorage.deleteUserById(id);
    }

    @Override
    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }
}
