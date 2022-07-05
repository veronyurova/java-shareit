package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final static String UNNECESSARY_ID_MESSAGE = "An id was passed for new user";
    private final static String EMPTY_ID_MESSAGE = "An empty user id was passed";

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
    public User addUser(User user) {
        if (user.getId() != null) {
            log.warn("ValidationException at UserService.addUser: {}", UNNECESSARY_ID_MESSAGE);
            throw new ValidationException(UNNECESSARY_ID_MESSAGE);
        }
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(Long id, User newUser) {
        if (newUser.getId() == null) {
            log.warn("ValidationException at UserService.updateUser: {}", EMPTY_ID_MESSAGE);
            throw new ValidationException(EMPTY_ID_MESSAGE);
        }
        getUserById(newUser.getId());
        return userStorage.updateUser(newUser);
    }

    @Override
    public void deleteUserById(Long id) {
        userStorage.deleteUserById(id);
    }
}
