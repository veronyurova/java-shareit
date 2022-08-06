package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager manager;
    @Autowired
    private ItemRequestRepository repository;
    private final User user1 = new User(1L, "User 1", "user1@mail.com");
    private final User user2 = new User(2L, "User 2", "user2@mail.com");
    private final User userAdd1 = new User(null, "User 1", "user1@mail.com");
    private final User userAdd2 = new User(null, "User 2", "user2@mail.com");
    private final ItemRequest itemRequestAdd1 = new ItemRequest(null, "Request 1", user1,
            LocalDateTime.MIN);
    private final ItemRequest itemRequestAdd2 = new ItemRequest(null, "Request 2", user1,
            LocalDateTime.MIN);
    private final ItemRequest itemRequestAdd3 = new ItemRequest(null, "Request 3", user2,
            LocalDateTime.MIN);
    private final ItemRequest itemRequestExpected1 = new ItemRequest(1L, "Request 1", user1,
            LocalDateTime.MIN);
    private final ItemRequest itemRequestExpected2 = new ItemRequest(2L, "Request 2", user1,
            LocalDateTime.MIN);
    private final ItemRequest itemRequestExpected3 = new ItemRequest(3L, "Request 3", user2,
            LocalDateTime.MIN);

    @Test
    void findAll() {
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemRequestAdd1);
        manager.persist(itemRequestAdd2);
        manager.persist(itemRequestAdd3);

        List<ItemRequest> requests = repository.findAll(2L, PageRequest.of(0, 10));

        assertNotNull(requests);
        assertEquals(2, requests.size());
        assertEquals(itemRequestExpected1, requests.get(0));
        assertEquals(itemRequestExpected2, requests.get(1));
    }

    @Test
    void findByRequesterId() {
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemRequestAdd1);
        manager.persist(itemRequestAdd2);
        manager.persist(itemRequestAdd3);

        List<ItemRequest> requests = repository.findByRequesterId(1L);

        assertNotNull(requests);
        assertEquals(2, requests.size());
        assertEquals(itemRequestExpected1, requests.get(0));
        assertEquals(itemRequestExpected2, requests.get(1));
    }
}
