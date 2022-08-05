package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private TestEntityManager manager;
    @Autowired
    private ItemRepository repository;
    private final User user1 = new User(1L, "User 1", "user1@mail.com");
    private final User user2 = new User(2L, "User 2", "user2@mail.com");
    private final User userAdd1 = new User(null, "User 1", "user1@mail.com");
    private final User userAdd2 = new User(null, "User 2", "user2@mail.com");
    private final Item itemAdd1 = new Item(null, "Item 1", "Test", true, user1, null);
    private final Item itemAdd2 = new Item(null, "Item 2", "Test", true, user2, null);
    private final Item itemExpected = new Item(1L, "Item 1", "Test", true, user1, null);

    @Test
    void findByOwnerId() {
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);

        List<Item> items = repository.findByOwnerId(1L, PageRequest.of(0, 10));

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(itemExpected, items.get(0));
    }

    @Test
    void searchItemsUpperAndLowerCase() {
        manager.persist(userAdd1);
        manager.persist(itemAdd1);

        List<Item> items = repository.searchItems("ItEm 1", PageRequest.of(0, 10));

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(itemExpected, items.get(0));
    }

    @Test
    void searchItemsOneWord() {
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);

        List<Item> items = repository.searchItems("ItEm", PageRequest.of(0, 10));

        assertNotNull(items);
        assertEquals(2, items.size());
    }
}
