package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private ItemService itemService;
    @Mock
    private ItemRepository mockItemRepository;
    @Mock
    private UserService mockUserService;
    @Mock
    private BookingRepository mockBookingRepository;
    @Mock
    private CommentRepository mockCommentRepository;
    @Mock
    private ItemRequestRepository mockItemRequestRepository;
    private final User user = new User(1L, "User", "user@mail.com");
    private final UserDto userDto = new UserDto(1L, "User", "user@mail.com");
    private final CommentDto commentDtoSave = new CommentDto(null, "Comment", "User", null);
    private final Item itemSave = new Item(null, "Item 1", "Test", true, user, null);
    private final Item itemGet = new Item(1L, "Item 1", "Test", true, user, null);
    private final ItemDto itemDtoSave = new ItemDto(null, "Item 1", "Test", true,
            null, null, null, null, null);
    private final ItemDto itemExpected = new ItemDto(1L, "Item 1", "Test", true,
            new ItemDto.UserDto(1L, "User"), null, null, null, null);
    private final ItemDto itemExpectedComments = new ItemDto(1L, "Item 1", "Test", true,
            new ItemDto.UserDto(1L, "User"), null, null, null, Collections.emptyList());

    @BeforeEach
    void beforeEach() {
        itemService = new ItemServiceImpl(mockItemRepository, mockUserService,
                mockBookingRepository, mockCommentRepository, mockItemRequestRepository);
    }

    @Test
    void getOwnerItems() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockItemRepository.findByOwnerId(1L, PageRequest.of(0, 10)))
                .thenReturn(List.of(itemGet));

        List<ItemDto> items = itemService.getOwnerItems(1L, 0, 10);

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(itemExpectedComments, items.get(0));
    }

    @Test
    void getOwnerItemsNoItems() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockItemRepository.findByOwnerId(1L, PageRequest.of(0, 10)))
                .thenReturn(Collections.emptyList());

        List<ItemDto> items = itemService.getOwnerItems(1L, 0, 10);

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void searchItems() {
        ItemDto itemExpectedSearch = new ItemDto(1L, "Item 1", "Test", true,
                new ItemDto.UserDto(1L, "User"), null, null, null, null);

        Mockito
                .when(mockItemRepository.searchItems("Item 1", PageRequest.of(0, 10)))
                .thenReturn(List.of(itemGet));

        List<ItemDto> items = itemService.searchItems("Item 1", 0, 10);

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(itemExpectedSearch, items.get(0));
    }

    @Test
    void searchItemsEmptyText() {
        List<ItemDto> items = itemService.searchItems("  ", 0, 10);

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void searchItemsNoItems() {
        Mockito
                .when(mockItemRepository.searchItems("Item 1", PageRequest.of(0, 10)))
                .thenReturn(Collections.emptyList());

        List<ItemDto> items = itemService.searchItems("Item 1", 0, 10);

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void getItemById() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(mockItemRepository.findById(1L)).thenReturn(Optional.of(itemGet));

        ItemDto item = itemService.getItemById(1L, 1L);

        assertNotNull(item);
        assertEquals(itemExpectedComments, item);
    }

    @Test
    void getItemByIdNoSuchItem() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(mockItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.getItemById(1L, 1L));
    }

    @Test
    void addItem() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(userDto);
        Mockito.when(mockItemRepository.save(itemSave)).thenReturn(itemGet);

        ItemDto item = itemService.addItem(1L, itemDtoSave);

        assertNotNull(item);
        assertEquals(itemExpected, item);
    }

    @Test
    void addItemWithRequest() {
        ItemRequest itemRequest = new ItemRequest(1L, "Request", user, null);
        Item itemSaveRequest = new Item(null, "Item 1", "Test", true, user, itemRequest);
        Item itemGetRequest = new Item(1L, "Item 1", "Test", true, user, itemRequest);
        ItemDto itemDtoSaveRequest = new ItemDto(null, "Item 1", "Test", true,
                null, 1L, null, null, null);
        ItemDto itemExpectedRequest = new ItemDto(1L, "Item 1", "Test", true,
                new ItemDto.UserDto(1L, "User"), 1L, null, null, null);

        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(userDto);
        Mockito
                .when(mockItemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest));
        Mockito.when(mockItemRepository.save(itemSaveRequest)).thenReturn(itemGetRequest);

        ItemDto item = itemService.addItem(1L, itemDtoSaveRequest);

        assertNotNull(item);
        assertEquals(itemExpectedRequest, item);
    }

    @Test
    void updateItem() {
        Item itemUpd = new Item(1L, "UPD", "UPD", false, user, null);
        ItemDto itemDtoUpd = new ItemDto(null, "UPD", "UPD", false,
                null, null, null, null, null);
        ItemDto itemExpectedUpd = new ItemDto(1L, "UPD", "UPD", false,
                new ItemDto.UserDto(1L, "User"), null, null, null, null);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemGet));
        Mockito.when(mockItemRepository.save(itemUpd)).thenReturn(itemUpd);

        ItemDto item = itemService.updateItem(1L, 1L, itemDtoUpd);

        assertNotNull(item);
        assertEquals(itemExpectedUpd, item);
    }

    @Test
    void updateName() {
        Item itemUpd = new Item(1L, "UPD", "Test", true, user, null);
        ItemDto itemDtoUpd = new ItemDto(null, "UPD", null, null,
                null, null, null, null, null);
        ItemDto itemExpectedUpd = new ItemDto(1L, "UPD", "Test", true,
                new ItemDto.UserDto(1L, "User"), null, null, null, null);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemGet));
        Mockito.when(mockItemRepository.save(itemUpd)).thenReturn(itemUpd);

        ItemDto item = itemService.updateItem(1L, 1L, itemDtoUpd);

        assertNotNull(item);
        assertEquals(itemExpectedUpd, item);
    }

    @Test
    void updateDescription() {
        Item itemUpd = new Item(1L, "Item 1", "UPD", true, user, null);
        ItemDto itemDtoUpd = new ItemDto(null, null, "UPD", null,
                null, null, null, null, null);
        ItemDto itemExpectedUpd = new ItemDto(1L, "Item 1", "UPD", true,
                new ItemDto.UserDto(1L, "User"), null, null, null, null);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemGet));
        Mockito.when(mockItemRepository.save(itemUpd)).thenReturn(itemUpd);

        ItemDto item = itemService.updateItem(1L, 1L, itemDtoUpd);

        assertNotNull(item);
        assertEquals(itemExpectedUpd, item);
    }

    @Test
    void updateAvailable() {
        Item itemUpd = new Item(1L, "Item 1", "Test", false, user, null);
        ItemDto itemDtoUpd = new ItemDto(null, null, null, false,
                null, null, null, null, null);
        ItemDto itemExpectedUpd = new ItemDto(1L, "Item 1", "Test", false,
                new ItemDto.UserDto(1L, "User"), null, null, null, null);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemGet));
        Mockito.when(mockItemRepository.save(itemUpd)).thenReturn(itemUpd);

        ItemDto item = itemService.updateItem(1L, 1L, itemDtoUpd);

        assertNotNull(item);
        assertEquals(itemExpectedUpd, item);
    }

    @Test
    void updateItemBlankNameAndDescription() {
        ItemDto itemDtoUpd = new ItemDto(null, "   ", "   ", true,
                null, null, null, null, null);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemGet));
        Mockito.when(mockItemRepository.save(itemGet)).thenReturn(itemGet);

        ItemDto item = itemService.updateItem(1L, 1L, itemDtoUpd);

        assertNotNull(item);
        assertEquals(itemExpected, item);
    }

    @Test
    void updateItemIncorrectId() {
        ItemDto itemUpd = new ItemDto(null, "UPD", "UPD", false,
                null, null, null, null, null);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.updateItem(1L, 2L, itemUpd));
    }

    @Test
    void updateItemIncorrectUserId() {
        ItemDto itemUpd = new ItemDto(null, "UPD", "UPD", false,
                null, null, null, null, null);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemGet));

        assertThrows(AccessDeniedException.class, () -> itemService.updateItem(2L, 1L, itemUpd));
    }

    @Test
    void deleteItemByIdIncorrectItemId() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.deleteItemById(1L, 1L));
    }

    @Test
    void deleteItemByIdIncorrectUserId() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemGet));

        assertThrows(AccessDeniedException.class, () -> itemService.deleteItemById(2L, 1L));
    }

    @Test
    void addComment() {
        Booking booking = new Booking();
        Comment commentGet = new Comment(1L, "Comment", itemGet, user, null);
        CommentDto commentExpected = new CommentDto(1L, "Comment", "User", null);

        Mockito
                .when(mockBookingRepository.findCompletedBooking(
                        Mockito.anyLong(),
                        Mockito.anyLong(),
                        Mockito.any(BookingStatus.class),
                        Mockito.any(LocalDateTime.class)
                        )
                )
                .thenReturn(booking);
        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemGet));
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(userDto);
        Mockito
                .when(mockCommentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(commentGet);

        CommentDto comment = itemService.addComment(1L, 1L, commentDtoSave);

        assertNotNull(comment);
        assertEquals(commentExpected, comment);
    }

    @Test
    void addCommentNoBooking() {
        Mockito
                .when(mockBookingRepository.findCompletedBooking(
                                Mockito.anyLong(),
                                Mockito.anyLong(),
                                Mockito.any(BookingStatus.class),
                                Mockito.any(LocalDateTime.class)
                        )
                )
                .thenReturn(null);

        assertThrows(ValidationException.class, () -> itemService.addComment(1L, 1L,
                commentDtoSave));
    }
}
