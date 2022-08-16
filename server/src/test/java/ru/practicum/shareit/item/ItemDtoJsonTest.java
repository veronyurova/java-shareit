package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoJsonTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testItemDto() throws IOException {
        ItemDto itemDto = new ItemDto(1L, "Item 1", "Test", true,
                new ItemDto.UserDto(1L, "User"), 1L, null, null, null);

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Item 1");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo("User");
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.lastBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathValue("$.nextBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathValue("$.comments").isEqualTo(null);
    }

    @Test
    void testItemDtoWithComments() throws IOException {
        CommentDto commentDto = new CommentDto(1L, "Comment", "User", LocalDateTime.MIN);
        ItemDto itemDto = new ItemDto(1L, "Item 1", "Test", true,
                new ItemDto.UserDto(1L, "User"), 1L, null, null, null);
        itemDto.setComments(List.of(commentDto));

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Item 1");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo("User");
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.lastBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathValue("$.nextBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text")
                .isEqualTo("Comment");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName")
                .isEqualTo("User");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].created")
                .isEqualTo("-999999999-01-01T00:00:00");
    }

    @Test
    void testItemDtoWithCommentsEmpty() throws IOException {
        ItemDto itemDto = new ItemDto(1L, "Item 1", "Test", true,
                new ItemDto.UserDto(1L, "User"), 1L, null, null, null);
        itemDto.setComments(Collections.emptyList());

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Item 1");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo("User");
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.lastBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathValue("$.nextBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathValue("$.comments")
                .isEqualTo(Collections.emptyList());
    }

    @Test
    void testItemDtoWithBookings() throws IOException {
        User user = new User(1L, "User", "user@mail.com");
        Item item = new Item(1L, "Item 1", "Test", true, user, null);
        Booking lastBooking = new Booking(1L, LocalDateTime.MIN, LocalDateTime.MIN,
                item, user, BookingStatus.APPROVED);
        Booking nextBooking = new Booking(2L, LocalDateTime.MAX, LocalDateTime.MAX,
                item, user, BookingStatus.APPROVED);
        ItemDto itemDto = new ItemDto(1L, "Item 1", "Test", true,
                new ItemDto.UserDto(1L, "User"), 1L, null, null, null);
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Item 1");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo("User");
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.start")
                .isEqualTo("-999999999-01-01T00:00:00");
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.end")
                .isEqualTo("-999999999-01-01T00:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.start")
                .isEqualTo("+999999999-12-31T23:59:59.999999999");
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.end")
                .isEqualTo("+999999999-12-31T23:59:59.999999999");
        assertThat(result).extractingJsonPathValue("$.comments").isEqualTo(null);
    }
}
