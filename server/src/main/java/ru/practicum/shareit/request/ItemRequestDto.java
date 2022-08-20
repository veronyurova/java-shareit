package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.item.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private UserDto requester;
    private LocalDateTime created;
    private List<ItemDto> items;

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    static class UserDto {
        private Long id;
        private String name;
    }
}
