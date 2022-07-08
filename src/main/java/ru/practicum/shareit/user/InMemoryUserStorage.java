package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.EmailAlreadyTakenException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long nextUserId = 0;

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(Long id) {
        if (!users.containsKey(id)) {
            String message = String.format("There is no user with id %d", id);
            log.warn("UserNotFoundException at InMemoryUserStorage.findUserById: {}", message);
            throw new UserNotFoundException(message);
        }
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        checkEmailAvailability(user.getEmail());
        user.setId(getUserId());
        users.put(user.getId(), user);
        log.info("InMemoryUserStorage.addUser: user {} " +
                 "successfully added to storage", user.getId());
        return user;
    }

    @Override
    public User updateUser(Long id, User newUser) {
        User user = findUserById(id);
        if (newUser.getName() != null && !newUser.getName().isBlank()) {
            user.setName(newUser.getName());
        }
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
            if (!user.getEmail().equals(newUser.getEmail())) {
                checkEmailAvailability(newUser.getEmail());
            }
            user.setEmail(newUser.getEmail());
        }
        log.info("InMemoryUserStorage.updateUser: user {} " +
                 "successfully updated", user.getId());
        return user;
    }

    @Override
    public void deleteUserById(Long id) {
        findUserById(id);
        users.remove(id);
        log.info("InMemoryUserStorage.deleteUserById: user {} " +
                 "successfully deleted from storage", id);
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
        nextUserId = 0;
    }

    private void checkEmailAvailability(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                String message = String.format("Email %s is already taken", email);
                log.warn("EmailAlreadyTakenException at InMemoryUserStorage.addUser: {}", message);
                throw new EmailAlreadyTakenException(message);
            }
        }
    }

    private Long getUserId() {
        nextUserId += 1;
        return nextUserId;
    }
}
