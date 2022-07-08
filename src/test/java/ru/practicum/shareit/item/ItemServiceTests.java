package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.AccessDeniedException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTests {
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;
    private final User user1 = new User(null, "User 1", "user1@yandex.ru");
    private final User user2 = new User(null, "User 2", "user2@yandex.ru");
    private final Item item1 = new Item(null, "Item 1", "Test", true, null, null);
    private final Item item2 = new Item(null, "Item 2", "Test", true, null, null);
    private final Item item3 = new Item(null, "Item 3", "Test", true, null, null);

    @AfterEach
    void afterEach() {
        itemService.deleteAllItems();
        userService.deleteAllUsers();
    }

    @Test
    void getOwnerItems() {
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);
        itemService.addItem(1L, item2);
        itemService.addItem(2L, item3);
        Item itemExp1 = new Item(1L, "Item 1", "Test", true, user1, null);
        Item itemExp2 = new Item(2L, "Item 2", "Test", true, user1, null);
        List<Item> itemsExpected = List.of(itemExp1, itemExp2);

        List<Item> items = itemService.getOwnerItems(1L);

        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(itemsExpected, items);
    }

    @Test
    void getOwnerItemsNoItems() {
        userService.addUser(user1);

        List<Item> items = itemService.getOwnerItems(1L);

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void getOwnerItemsNoSuchUser() {
        List<Item> items = itemService.getOwnerItems(1L);

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void searchItemsNameUpperCase() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        Item itemExp = new Item(1L, "Item 1", "Test", true, user1, null);
        List<Item> itemsExpected = List.of(itemExp);

        List<Item> items = itemService.searchItems("ITEM 1");

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(itemsExpected, items);
    }

    @Test
    void searchItemsNameLowerCase() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        Item itemExp = new Item(1L, "Item 1", "Test", true, user1, null);
        List<Item> itemsExpected = List.of(itemExp);

        List<Item> items = itemService.searchItems("item 1");

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(itemsExpected, items);
    }

    @Test
    void searchItemsDescriptionUpperCase() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        Item itemExp = new Item(1L, "Item 1", "Test", true, user1, null);
        List<Item> itemsExpected = List.of(itemExp);

        List<Item> items = itemService.searchItems("TEST");

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(itemsExpected, items);
    }

    @Test
    void searchItemsDescriptionLowerCase() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        Item itemExp = new Item(1L, "Item 1", "Test", true, user1, null);
        List<Item> itemsExpected = List.of(itemExp);

        List<Item> items = itemService.searchItems("test");

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(itemsExpected, items);
    }

    @Test
    void searchItemsItemNotAvailable() {
        Item item = new Item(1L, "Item 1", "Test", false, user1, null);
        userService.addUser(user1);
        itemService.addItem(1L, item);

        List<Item> items = itemService.searchItems("Item 1");

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void searchItemsEmptyText() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);

        List<Item> items = itemService.searchItems("");

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void searchItemsNoItems() {
        List<Item> items = itemService.searchItems("item");

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void getItemById() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        Item itemExp = new Item(1L, "Item 1", "Test", true, user1, null);

        Item item = itemService.getItemById(1L);

        assertNotNull(item);
        assertEquals(itemExp, item);
    }

    @Test
    void getItemByIdNoSuchItem() {
        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(1L));
    }

    @Test
    void addItem() {
        userService.addUser(user1);
        Item itemExp = new Item(1L, "Item 1", "Test", true, user1, null);

        Item item = itemService.addItem(1L, item1);

        assertNotNull(item);
        assertEquals(itemExp, item);
    }

    @Test
    void addItemNoName() {
        userService.addUser(user1);
        Item item = new Item(1L, null, "Test", true, user1, null);

        assertThrows(ConstraintViolationException.class, () -> itemService.addItem(1L, item));
    }

    @Test
    void addItemNoDescription() {
        userService.addUser(user1);
        Item item = new Item(1L, "Item 1", null, true, user1, null);

        assertThrows(ConstraintViolationException.class, () -> itemService.addItem(1L, item));
    }

    @Test
    void addItemNoAvailable() {
        userService.addUser(user1);
        Item item = new Item(1L, "Item 1", "Test", null, user1, null);

        assertThrows(ConstraintViolationException.class, () -> itemService.addItem(1L, item));
    }

    @Test
    void addItemNoSuchUser() {
        assertThrows(UserNotFoundException.class, () -> itemService.addItem(1L, item1));
    }

    @Test
    void updateItem() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        Item itemUpd = new Item(null, "UPD", "UPD", false, null, null);
        Item itemExp = new Item(1L, "UPD", "UPD", false, user1, null);

        Item item = itemService.updateItem(1L, 1L, itemUpd);

        assertNotNull(item);
        assertEquals(itemExp, item);
    }

    @Test
    void updateName() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        Item itemUpd = new Item(null, "UPD", null, null, null, null);
        Item itemExp = new Item(1L, "UPD", "Test", true, user1, null);

        Item item = itemService.updateItem(1L, 1L, itemUpd);

        assertNotNull(item);
        assertEquals(itemExp, item);
    }

    @Test
    void updateDescription() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        Item itemUpd = new Item(null, null, "UPD", null, null, null);
        Item itemExp = new Item(1L, "Item 1", "UPD", true, user1, null);

        Item item = itemService.updateItem(1L, 1L, itemUpd);

        assertNotNull(item);
        assertEquals(itemExp, item);
    }

    @Test
    void updateAvailable() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        Item itemUpd = new Item(null, null, null, false, null, null);
        Item itemExp = new Item(1L, "Item 1", "Test", false, user1, null);

        Item item = itemService.updateItem(1L, 1L, itemUpd);

        assertNotNull(item);
        assertEquals(itemExp, item);
    }

    @Test
    void updateItemBlankNameAndDescription() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        Item itemUpd = new Item(null, "   ", "  ", false, null, null);
        Item itemExp = new Item(1L, "Item 1", "Test", false, user1, null);

        Item item = itemService.updateItem(1L, 1L, itemUpd);

        assertNotNull(item);
        assertEquals(itemExp, item);
    }

    @Test
    void updateItemIncorrectId() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        Item itemUpd = new Item(null, "UPD", "UPD", false, null, null);

        assertThrows(ItemNotFoundException.class, () -> itemService.updateItem(1L, 2L, itemUpd));
    }

    @Test
    void updateItemIncorrectUserId() {
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);
        Item itemUpd = new Item(null, "UPD", "UPD", false, null, null);

        assertThrows(AccessDeniedException.class, () -> itemService.updateItem(2L, 1L, itemUpd));
    }

    @Test
    void deleteItemById() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);

        itemService.deleteItemById(1L, 1L);

        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(1L));
    }

    @Test
    void deleteItemByIdIncorrectUserId() {
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);

        assertThrows(AccessDeniedException.class, () -> itemService.deleteItemById(2L, 1L));
    }
}
