package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            String message = String.format("There is no user with id %d", userId);
            log.warn("EntityNotFoundException at UserServiceImpl.getUserById: {}", message);
            throw new EntityNotFoundException(message);
        }
        return UserMapper.toUserDto(user.get());
    }

    @Override
    public UserDto addUser(@Valid UserDto userDto) {
        User user = UserMapper.toUserAdd(userDto);
        User addedUser = userRepository.save(user);
        log.info("UserServiceImpl.addUser: user {} successfully added", addedUser.getId());
        return UserMapper.toUserDto(addedUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            String message = String.format("There is no user with id %d", userId);
            log.warn("EntityNotFoundException at UserServiceImpl.updateUser: {}", message);
            throw new EntityNotFoundException(message);
        }
        User user = userOptional.get();
        @Valid User newUser = UserMapper.toUserAdd(userDto);
        if (newUser.getName() != null && !newUser.getName().isBlank()) {
            user.setName(newUser.getName());
        }
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
            user.setEmail(newUser.getEmail());
        }
        User updatedUser = userRepository.save(user);
        log.info("UserServiceImpl.updateUser: user {} successfully updated", user.getId());
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUserById(Long userId) {
        getUserById(userId);
        userRepository.deleteById(userId);
        log.info("UserServiceImpl.deleteUserById: user {} successfully deleted", userId);
    }
}
