package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTests {
    private final UserServiceImpl userService;
    private final User user1 = new User(null, "User 1", "user1@yandex.ru");
    private final User user2 = new User(null, "User 2", "user2@yandex.ru");

    @AfterEach
    void afterEach() {
        userService.deleteAllUsers();
    }

    @Test
    void getAllUsers() {
        userService.addUser(user1);
        userService.addUser(user2);
        User userExp1 = new User(1L, "User 1", "user1@yandex.ru");
        User userExp2 = new User(2L, "User 2", "user2@yandex.ru");
        List<User> usersExpected = List.of(userExp1, userExp2);

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(usersExpected, users);
    }

    @Test
    void getAllUsersNoUsers() {
        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(0, users.size());
    }

    @Test
    void getUserById() {
        userService.addUser(user1);
        User userExp = new User(1L, "User 1", "user1@yandex.ru");

        User user = userService.getUserById(1L);

        assertNotNull(user);
        assertEquals(userExp, user);
    }

    @Test
    void getUserByIdNoSuchUser() {
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void addUser() {
        User userExp = new User(1L, "User 1", "user1@yandex.ru");

        User user = userService.addUser(user1);

        assertNotNull(user);
        assertEquals(userExp, user);
    }

    @Test
    void addUserDuplicateEmail() {
        userService.addUser(user1);
        User userEmail = new User(null, "User", "user1@yandex.ru");

        assertThrows(SQLException.class, () -> userService.addUser(userEmail));
    }

    @Test
    void addUserIncorrectEmail() {
        User user = new User(null, "User", "@yandex.ru");

        assertThrows(ConstraintViolationException.class, () -> userService.addUser(user));
    }

    @Test
    void addUserNoName() {
        User user = new User(null, null, "user1@yandex.ru");

        assertThrows(ConstraintViolationException.class, () -> userService.addUser(user));
    }

    @Test
    void addUserNoEmail() {
        User user = new User(null, "User", null);

        assertThrows(ConstraintViolationException.class, () -> userService.addUser(user));
    }

    @Test
    void updateUser() {
        userService.addUser(user1);
        User userUpd = new User(null, "UPD", "upd@yandex.ru");
        User userExp = new User(1L, "UPD", "upd@yandex.ru");

        User user = userService.updateUser(1L, userUpd);

        assertNotNull(user);
        assertEquals(userExp, user);
    }

    @Test
    void updateName() {
        userService.addUser(user1);
        User userUpd = new User(null, "UPD", null);
        User userExp = new User(1L, "UPD", "user1@yandex.ru");

        User user = userService.updateUser(1L, userUpd);

        assertNotNull(user);
        assertEquals(userExp, user);
    }

    @Test
    void updateEmail() {
        userService.addUser(user1);
        User userUpd = new User(null, null, "upd@yandex.ru");
        User userExp = new User(1L, "User 1", "upd@yandex.ru");

        User user = userService.updateUser(1L, userUpd);

        assertNotNull(user);
        assertEquals(userExp, user);
    }

    @Test
    void updateUserBlankNameAndEmail() {
        userService.addUser(user1);
        User userUpd = new User(null, "  ", "  ");
        User userExp = new User(1L, "User 1", "user1@yandex.ru");

        User user = userService.updateUser(1L, userUpd);

        assertNotNull(user);
        assertEquals(userExp, user);
    }

    @Test
    void updateDuplicateEmail() {
        userService.addUser(user1);
        userService.addUser(user2);
        User userUpd = new User(null, null, "user2@yandex.ru");

        assertThrows(SQLException.class, () -> userService.updateUser(1L, userUpd));
    }

    @Test
    void updateIncorrectId() {
        userService.addUser(user1);
        User userUpd = new User(null, "UPD", "upd@yandex.ru");

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
