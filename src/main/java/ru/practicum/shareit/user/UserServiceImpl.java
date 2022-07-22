package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Validated
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            String message = String.format("There is no user with id %d", id);
            log.warn("EntityNotFoundException at UserServiceImpl.getUserById: {}", message);
            throw new EntityNotFoundException(message);
        }
        return user.get();
    }

    @Override
    public User addUser(@Valid User user) {
        User addedUser = userRepository.save(user);
        log.info("UserServiceImpl.addUser: user {} successfully added", addedUser.getId());
        return addedUser;
    }

    @Override
    public User updateUser(Long id, User newUser) {
        User user = getUserById(id);
        if (newUser.getName() != null && !newUser.getName().isBlank()) {
            user.setName(newUser.getName());
        }
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
            user.setEmail(newUser.getEmail());
        }
        User updatedUser = userRepository.save(user);
        log.info("UserServiceImpl.updateUser: user {} successfully updated", user.getId());
        return updatedUser;
    }

    @Override
    public void deleteUserById(Long id) {
        getUserById(id);
        userRepository.deleteById(id);
        log.info("UserServiceImpl.deleteUserById: user {} successfully deleted", id);
    }
}
