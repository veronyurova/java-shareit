package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;

    @Getter
    @AllArgsConstructor
    static class User {
        private Long id;
        private String name;
    }
}
