package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTests {
    private final UserService userService;
    private final UserDto user1 = new UserDto(null, "User 1", "user1@yandex.ru");
    private final UserDto user2 = new UserDto(null, "User 2", "user2@yandex.ru");

    @Test
    void getAllUsers() {
        userService.addUser(user1);
        userService.addUser(user2);

        List<UserDto> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void getAllUsersNoUsers() {
        List<UserDto> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(0, users.size());
    }

    @Test
    void getUserById() {
        userService.addUser(user1);

        UserDto user = userService.getUserById(1L);

        assertNotNull(user);
        assertEquals(1L, user.getId());
    }

    @Test
    void getUserByIdNoSuchUser() {
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void addUser() {
        UserDto user = userService.addUser(user1);

        assertNotNull(user);
        assertEquals(1L, user.getId());
    }

    @Test
    void addUserDuplicateEmail() {
        userService.addUser(user1);
        UserDto userEmail = new UserDto(null, "User", "user1@yandex.ru");

        assertThrows(DataIntegrityViolationException.class, () -> userService.addUser(userEmail));
    }

    @Test
    void addUserIncorrectEmail() {
        UserDto user = new UserDto(null, "User", "@yandex.ru");

        assertThrows(ConstraintViolationException.class, () -> userService.addUser(user));
    }

    @Test
    void addUserNoName() {
        UserDto user = new UserDto(null, null, "user1@yandex.ru");

        assertThrows(ConstraintViolationException.class, () -> userService.addUser(user));
    }

    @Test
    void addUserNoEmail() {
        UserDto user = new UserDto(null, "User", null);

        assertThrows(ConstraintViolationException.class, () -> userService.addUser(user));
    }

    @Test
    void updateUser() {
        userService.addUser(user1);
        UserDto userUpd = new UserDto(null, "UPD", "upd@yandex.ru");

        UserDto user = userService.updateUser(1L, userUpd);

        assertNotNull(user);
        assertEquals("UPD", user.getName());
        assertEquals("upd@yandex.ru", user.getEmail());
    }

    @Test
    void updateName() {
        userService.addUser(user1);
        UserDto userUpd = new UserDto(null, "UPD", null);

        UserDto user = userService.updateUser(1L, userUpd);

        assertNotNull(user);
        assertEquals("UPD", user.getName());
        assertEquals("user1@yandex.ru", user.getEmail());
    }

    @Test
    void updateEmail() {
        userService.addUser(user1);
        UserDto userUpd = new UserDto(null, null, "upd@yandex.ru");

        UserDto user = userService.updateUser(1L, userUpd);

        assertNotNull(user);
        assertEquals("User 1", user.getName());
        assertEquals("upd@yandex.ru", user.getEmail());
    }

    @Test
    void updateUserBlankNameAndEmail() {
        userService.addUser(user1);
        UserDto userUpd = new UserDto(null, "  ", "  ");

        UserDto user = userService.updateUser(1L, userUpd);

        assertNotNull(user);
        assertEquals("User 1", user.getName());
        assertEquals("user1@yandex.ru", user.getEmail());
    }

    @Test
    void updateDuplicateEmail() {
        userService.addUser(user1);
        userService.addUser(user2);
        UserDto userUpd = new UserDto(null, null, "user2@yandex.ru");

        assertThrows(DataIntegrityViolationException.class,
                () -> userService.updateUser(1L, userUpd));
    }

    @Test
    void updateIncorrectId() {
        userService.addUser(user1);
        UserDto userUpd = new UserDto(null, "UPD", "upd@yandex.ru");

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(2L, userUpd));
    }

    @Test
    void deleteUserById() {
        userService.addUser(user1);

        userService.deleteUserById(1L);

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void deleteUserByIdNoSuchUser() {
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUserById(1L));
    }
}
