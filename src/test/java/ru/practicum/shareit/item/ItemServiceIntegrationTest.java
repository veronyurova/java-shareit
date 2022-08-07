package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemServiceIntegrationTest {
    private final EntityManager manager;
    private final ItemService itemService;
    private final User user = new User(1L, "User", "user@mail.com");
    private final Item item = new Item(1L, "Item", "Test", true, user, null);
    private final User userAdd = new User(null, "User", "user@mail.com");
    private final Item itemAdd = new Item(null, "Item", "Test", true, user, null);
    private final ItemDto itemExpected = new ItemDto(1L, "Item 1", "Test", true,
            new ItemDto.UserDto(1L, "User"), null, null, null, null);
    private final Item itemAdd1 = new Item(null, "Item 1", "Test", true, user, null);
    private final Item itemAdd2 = new Item(null, "Item 2", "Test", true, user, null);
    private final ItemDto itemExpected1 = new ItemDto(1L, "Item 1", "Test", true,
            new ItemDto.UserDto(1L, "User"), null, null, null, Collections.emptyList());
    private final ItemDto itemExpected2 = new ItemDto(2L, "Item 2", "Test", true,
            new ItemDto.UserDto(1L, "User"), null, null, null, Collections.emptyList());

    @Test
    void getOwnerItems() {
        User otherUser = new User(2L, "User2", "user2@mail.com");
        User otherUserAdd = new User(null, "User2", "user2@mail.com");
        Item itemAddOtherUser = new Item(null, "Item", "Test", true, otherUser, null);
        manager.persist(userAdd);
        manager.persist(otherUserAdd);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(itemAddOtherUser);

        List<ItemDto> itemsExpected = List.of(itemExpected1, itemExpected2);
        List<ItemDto> items = itemService.getOwnerItems(1L, 0, 10);

        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(itemsExpected.get(0), items.get(0));
        assertEquals(itemsExpected.get(1), items.get(1));
    }

    @Test
    void searchItems() {
        manager.persist(userAdd);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);

        List<ItemDto> items = itemService.searchItems("Item 1", 0, 10);

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(itemExpected, items.get(0));
    }

    @Test
    void getItemById() {
        manager.persist(userAdd);
        manager.persist(itemAdd);

        ItemDto item = itemService.getItemById(1L, 1L);

        assertNotNull(item);
        assertEquals(itemExpected.getId(), item.getId());
    }

    @Test
    void addItem() {
        ItemDto itemDtoAdd = new ItemDto(null, "Item 1", "Test", true,
                null, null, null, null, null);
        manager.persist(userAdd);

        itemService.addItem(1L, itemDtoAdd);

        String queryString = "SELECT i FROM Item i WHERE i.id = 1";
        TypedQuery<Item> query = manager.createQuery(queryString, Item.class);
        Item item = query.getSingleResult();

        assertNotNull(item);
        assertEquals(1L, item.getId());
        assertEquals(itemDtoAdd.getName(), item.getName());
        assertEquals(itemDtoAdd.getDescription(), item.getDescription());
        assertEquals(itemDtoAdd.getAvailable(), item.getAvailable());
    }

    @Test
    void updateItem() {
        ItemDto itemDtoUpd = new ItemDto(null, "UPD", "UPD", false,
                null, null, null, null, null);
        manager.persist(userAdd);
        manager.persist(itemAdd);

        itemService.updateItem(1L, 1L, itemDtoUpd);

        String queryString = "SELECT i FROM Item i WHERE i.id = 1";
        TypedQuery<Item> query = manager.createQuery(queryString, Item.class);
        Item item = query.getSingleResult();

        assertNotNull(item);
        assertEquals(1L, item.getId());
        assertEquals(itemDtoUpd.getName(), item.getName());
        assertEquals(itemDtoUpd.getDescription(), item.getDescription());
        assertEquals(itemDtoUpd.getAvailable(), item.getAvailable());
    }

    @Test
    void deleteItemById() {
        manager.persist(userAdd);
        manager.persist(itemAdd);

        itemService.deleteItemById(1L, 1L);

        String queryString = "SELECT i FROM Item i WHERE i.id = 1";
        TypedQuery<Item> query = manager.createQuery(queryString, Item.class);

        assertThrows(NoResultException.class, () -> query.getSingleResult());
    }

    @Test
    void addComment() {
        CommentDto commentDtoAdd = new CommentDto(null, "Comment", "User", null);
        Booking bookingAdd = new Booking(null, LocalDateTime.now().minusDays(2L),
                LocalDateTime.now().minusDays(1L), item, user, BookingStatus.APPROVED);
        manager.persist(userAdd);
        manager.persist(itemAdd);
        manager.persist(bookingAdd);

        itemService.addComment(1L, 1L, commentDtoAdd);

        String queryString = "SELECT c FROM Comment c WHERE c.id = 1";
        TypedQuery<Comment> query = manager.createQuery(queryString, Comment.class);
        Comment comment = query.getSingleResult();

        assertNotNull(comment);
        assertEquals(1L, comment.getId());
        assertEquals(commentDtoAdd.getText(), comment.getText());
        assertEquals(commentDtoAdd.getAuthorName(), comment.getAuthor().getName());
    }
}
