package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.exception.AccessDeniedException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemServiceTests {
    private final ItemService itemService;
    private final UserService userService;
    private final UserDto user1 = new UserDto(null, "User 1", "user1@yandex.ru");
    private final UserDto user2 = new UserDto(null, "User 2", "user2@yandex.ru");
    private final ItemDto item1 = new ItemDto(null, "Item 1", "Test", true, null, null, null,
            null);
    private final ItemDto item2 = new ItemDto(null, "Item 2", "Test", true, null, null, null,
            null);
    private final ItemDto item3 = new ItemDto(null, "Item 3", "Test", true, null, null, null,
            null);

    @Test
    void getOwnerItems() {
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);
        itemService.addItem(1L, item2);
        itemService.addItem(2L, item3);

        List<ItemDto> items = itemService.getOwnerItems(1L);

        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(1L, items.get(0).getId());
        assertEquals(2L, items.get(1).getId());
    }

    @Test
    void getOwnerItemsNoItems() {
        userService.addUser(user1);

        List<ItemDto> items = itemService.getOwnerItems(1L);

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void getOwnerItemsNoSuchUser() {
        List<ItemDto> items = itemService.getOwnerItems(1L);

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void searchItemsNameUpperCase() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);

        List<ItemDto> items = itemService.searchItems("ITEM 1");

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getId());
    }

    @Test
    void searchItemsNameLowerCase() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);

        List<ItemDto> items = itemService.searchItems("item 1");

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getId());
    }

    @Test
    void searchItemsDescriptionUpperCase() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);

        List<ItemDto> items = itemService.searchItems("TEST");

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getId());
    }

    @Test
    void searchItemsDescriptionLowerCase() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);

        List<ItemDto> items = itemService.searchItems("test");

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getId());
    }

    @Test
    void searchItemsItemNotAvailable() {
        ItemDto itemUpd = new ItemDto(null, null, null, false, null, null, null, null);
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        itemService.updateItem(1L, 1L, itemUpd);

        List<ItemDto> items = itemService.searchItems("Item 1");

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void searchItemsEmptyText() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);

        List<ItemDto> items = itemService.searchItems("");

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void searchItemsNoItems() {
        List<ItemDto> items = itemService.searchItems("item");

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void getItemById() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);

        ItemDto item = itemService.getItemById(1L, 1L);

        assertNotNull(item);
        assertEquals(1L, item.getId());
    }

    @Test
    void getItemByIdNoSuchItem() {
        userService.addUser(user1);

        assertThrows(EntityNotFoundException.class, () -> itemService.getItemById(1L, 1L));
    }

    @Test
    void addItem() {
        userService.addUser(user1);

        ItemDto item = itemService.addItem(1L, item1);

        assertNotNull(item);
        assertEquals(1L, item.getId());
    }

    @Test
    void addItemNoName() {
        userService.addUser(user1);
        ItemDto item = new ItemDto(null, null, "Test", true, null, null, null, null);

        assertThrows(ConstraintViolationException.class, () -> itemService.addItem(1L, item));
    }

    @Test
    void addItemNoDescription() {
        userService.addUser(user1);
        ItemDto item = new ItemDto(null, "Item 1", null, true, null, null, null, null);

        assertThrows(ConstraintViolationException.class, () -> itemService.addItem(1L, item));
    }

    @Test
    void addItemNoAvailable() {
        userService.addUser(user1);
        ItemDto item = new ItemDto(null, "Item 1", "Test", null, null, null, null, null);

        assertThrows(ConstraintViolationException.class, () -> itemService.addItem(1L, item));
    }

    @Test
    void addItemNoSuchUser() {
        assertThrows(EntityNotFoundException.class, () -> itemService.addItem(1L, item1));
    }

    @Test
    void updateItem() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        ItemDto itemUpd = new ItemDto(null, "NAME", "DESC", false, null, null, null, null);

        ItemDto item = itemService.updateItem(1L, 1L, itemUpd);

        assertNotNull(item);
        assertEquals("NAME", item.getName());
        assertEquals("DESC", item.getDescription());
        assertEquals(false, item.getAvailable());
    }

    @Test
    void updateName() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        ItemDto itemUpd = new ItemDto(null, "NAME", null, null, null, null, null, null);

        ItemDto item = itemService.updateItem(1L, 1L, itemUpd);

        assertNotNull(item);
        assertEquals("NAME", item.getName());
        assertEquals("Test", item.getDescription());
        assertEquals(true, item.getAvailable());
    }

    @Test
    void updateDescription() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        ItemDto itemUpd = new ItemDto(null, null, "DESC", null, null, null, null, null);

        ItemDto item = itemService.updateItem(1L, 1L, itemUpd);

        assertNotNull(item);
        assertEquals("Item 1", item.getName());
        assertEquals("DESC", item.getDescription());
        assertEquals(true, item.getAvailable());
    }

    @Test
    void updateAvailable() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        ItemDto itemUpd = new ItemDto(null, null, null, false, null, null, null, null);

        ItemDto item = itemService.updateItem(1L, 1L, itemUpd);

        assertNotNull(item);
        assertEquals("Item 1", item.getName());
        assertEquals("Test", item.getDescription());
        assertEquals(false, item.getAvailable());
    }

    @Test
    void updateItemBlankNameAndDescription() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        ItemDto itemUpd = new ItemDto(null, "   ", "   ", false, null, null, null, null);

        ItemDto item = itemService.updateItem(1L, 1L, itemUpd);

        assertNotNull(item);
        assertEquals("Item 1", item.getName());
        assertEquals("Test", item.getDescription());
        assertEquals(false, item.getAvailable());
    }

    @Test
    void updateItemIncorrectId() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);
        ItemDto itemUpd = new ItemDto(null, "UPD", "UPD", false, null, null, null, null);

        assertThrows(EntityNotFoundException.class, () -> itemService.updateItem(1L, 2L, itemUpd));
    }

    @Test
    void updateItemIncorrectUserId() {
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);
        ItemDto itemUpd = new ItemDto(null, "UPD", "UPD", false, null, null, null, null);

        assertThrows(AccessDeniedException.class, () -> itemService.updateItem(2L, 1L, itemUpd));
    }

    @Test
    void deleteItemById() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);

        itemService.deleteItemById(1L, 1L);

        assertThrows(EntityNotFoundException.class, () -> itemService.getItemById(1L, 1L));
    }

    @Test
    void deleteItemByIdIncorrectUserId() {
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);

        assertThrows(AccessDeniedException.class, () -> itemService.deleteItemById(2L, 1L));
    }
}
