package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import org.mockito.Mock;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private UserService userService;
    @Mock
    private UserRepository mockUserRepository;
    private final User userSave = new User(null, "User", "user@mail.com");
    private final User userGet = new User(1L, "User", "user@mail.com");
    private final UserDto userDtoSave = new UserDto(null, "User", "user@mail.com");
    private final UserDto userExpected = new UserDto(1L, "User", "user@mail.com");


    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(mockUserRepository);
    }

    @Test
    void getAllUsers() {
        Mockito.when(mockUserRepository.findAll()).thenReturn(List.of(userGet));

        List<UserDto> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(userExpected, users.get(0));
    }

    @Test
    void getAllUsersNoUsers() {
        Mockito.when(mockUserRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDto> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(0, users.size());
    }

    @Test
    void getUserById() {
        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(userGet));

        UserDto user = userService.getUserById(1L);

        assertNotNull(user);
        assertEquals(userExpected, user);
    }

    @Test
    void getUserByIdNoSuchUser() {
        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void addUser() {
        Mockito.when(mockUserRepository.save(userSave)).thenReturn(userGet);

        UserDto user = userService.addUser(userDtoSave);

        assertNotNull(user);
        assertEquals(userExpected, user);
    }

    @Test
    void updateUser() {
        UserDto userDtoUpd = new UserDto(null, "UPD", "upd@mail.com");
        User userUpd = new User(1L, "UPD", "upd@mail.com");
        UserDto userExpectedUpd = new UserDto(1L, "UPD", "upd@mail.com");

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(userGet));
        Mockito.when(mockUserRepository.save(userUpd)).thenReturn(userUpd);

        UserDto user = userService.updateUser(1L, userDtoUpd);

        assertNotNull(user);
        assertEquals(userExpectedUpd, user);
    }

    @Test
    void updateName() {
        UserDto userDtoUpd = new UserDto(null, "UPD", null);
        User userUpd = new User(1L, "UPD", "user@mail.com");
        UserDto userExpectedUpd = new UserDto(1L, "UPD", "user@mail.com");

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(userGet));
        Mockito.when(mockUserRepository.save(userUpd)).thenReturn(userUpd);

        UserDto user = userService.updateUser(1L, userDtoUpd);

        assertNotNull(user);
        assertEquals(userExpectedUpd, user);
    }

    @Test
    void updateEmail() {
        UserDto userDtoUpd = new UserDto(null, null, "upd@mail.com");
        User userUpd = new User(1L, "User", "upd@mail.com");
        UserDto userExpectedUpd = new UserDto(1L, "User", "upd@mail.com");

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(userGet));
        Mockito.when(mockUserRepository.save(userUpd)).thenReturn(userUpd);

        UserDto user = userService.updateUser(1L, userDtoUpd);

        assertNotNull(user);
        assertEquals(userExpectedUpd, user);
    }

    @Test
    void updateUserBlankNameAndEmail() {
        UserDto userDtoUpd = new UserDto(null, "  ", "  ");

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(userGet));
        Mockito.when(mockUserRepository.save(userGet)).thenReturn(userGet);

        UserDto user = userService.updateUser(1L, userDtoUpd);

        assertNotNull(user);
        assertEquals(userExpected, user);
    }

    @Test
    void updateIncorrectId() {
        UserDto userUpd = new UserDto(null, "UPD", "upd@yandex.ru");

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(2L, userUpd));
    }

    @Test
    void deleteUserByIdNoSuchUser() {
        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUserById(1L));
    }
}
