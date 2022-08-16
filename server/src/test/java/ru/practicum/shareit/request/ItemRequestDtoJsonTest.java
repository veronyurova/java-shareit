package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.ItemDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testItemRequestDto() throws IOException {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Request",
                new ItemRequestDto.UserDto(1L, "User"), LocalDateTime.MIN, null);

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Request");
        assertThat(result).extractingJsonPathNumberValue("$.requester.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.requester.name").isEqualTo("User");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("-999999999-01-01T00:00:00");
        assertThat(result).extractingJsonPathValue("$.items").isEqualTo(null);
    }

    @Test
    void testItemRequestDtoWithItems() throws IOException {
        ItemDto itemDto = new ItemDto(1L, "Item 1", "Test", true,
                new ItemDto.UserDto(1L, "User"), 1L, null, null, null);
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Request",
                new ItemRequestDto.UserDto(1L, "User"), LocalDateTime.MIN, null);
        itemRequestDto.setItems(List.of(itemDto));

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Request");
        assertThat(result).extractingJsonPathNumberValue("$.requester.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.requester.name").isEqualTo("User");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("-999999999-01-01T00:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("Item 1");
        assertThat(result).extractingJsonPathStringValue("$.items[0].description")
                .isEqualTo("Test");
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.items[0].owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items[0].owner.name")
                .isEqualTo("User");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.items[0].lastBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathValue("$.items[0].nextBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathValue("$.items[0].comments").isEqualTo(null);
    }

    @Test
    void testItemRequestDtoWithItemsEmpty() throws IOException {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Request",
                new ItemRequestDto.UserDto(1L, "User"), LocalDateTime.MIN, null);
        itemRequestDto.setItems(Collections.emptyList());

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Request");
        assertThat(result).extractingJsonPathNumberValue("$.requester.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.requester.name").isEqualTo("User");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("-999999999-01-01T00:00:00");
        assertThat(result).extractingJsonPathValue("$.items").isEqualTo(Collections.emptyList());
    }
}
