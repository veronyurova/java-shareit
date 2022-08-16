package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIntegrationTest {
    private final EntityManager manager;
    private final UserService userService;
    private final User userAdd = new User(null, "User", "user@mail.com");
    private final UserDto userExpected = new UserDto(1L, "User", "user@mail.com");

    @Test
    void getAllUsers() {
        User userAdd1 = new User(null, "User 1", "user1@mail.com");
        User userAdd2 = new User(null, "User 2", "user2@mail.com");
        User userAdd3 = new User(null, "User 3", "user3@mail.com");
        UserDto userExpected1 = new UserDto(1L, "User 1", "user1@mail.com");
        UserDto userExpected2 = new UserDto(2L, "User 2", "user2@mail.com");
        UserDto userExpected3 = new UserDto(3L, "User 3", "user3@mail.com");

        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(userAdd3);

        List<UserDto> usersExpected = List.of(userExpected1, userExpected2, userExpected3);
        List<UserDto> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(3, users.size());
        assertEquals(usersExpected.get(0), users.get(0));
        assertEquals(usersExpected.get(1), users.get(1));
        assertEquals(usersExpected.get(2), users.get(2));
    }

    @Test
    void getUserById() {
        manager.persist(userAdd);

        UserDto user = userService.getUserById(1L);

        assertNotNull(user);
        assertEquals(userExpected, user);
    }

    @Test
    void addUser() {
        UserDto userDtoAdd = new UserDto(null, "User", "user@mail.com");

        userService.addUser(userDtoAdd);

        String queryString = "SELECT u FROM User u WHERE u.id = 1";
        TypedQuery<User> query = manager.createQuery(queryString, User.class);
        User user = query.getSingleResult();

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals(userDtoAdd.getName(), user.getName());
        assertEquals(userDtoAdd.getEmail(), user.getEmail());
    }

    @Test
    void updateUser() {
        UserDto userDtoUpd = new UserDto(null, "UPD", "upd@mail.com");
        manager.persist(userAdd);

        userService.updateUser(1L, userDtoUpd);

        String queryString = "SELECT u FROM User u WHERE u.id = 1";
        TypedQuery<User> query = manager.createQuery(queryString, User.class);
        User user = query.getSingleResult();

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals(userDtoUpd.getName(), user.getName());
        assertEquals(userDtoUpd.getEmail(), user.getEmail());
    }

    @Test
    void deleteUserById() {
        manager.persist(userAdd);

        userService.deleteUserById(1L);

        String queryString = "SELECT u FROM User u WHERE u.id = 1";
        TypedQuery<User> query = manager.createQuery(queryString, User.class);

        assertThrows(NoResultException.class, () -> query.getSingleResult());
    }
}
