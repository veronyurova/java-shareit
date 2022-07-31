package ru.practicum.shareit.user;

import javax.validation.Valid;
import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto addUser(@Valid UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    void deleteUserById(Long userId);
}
