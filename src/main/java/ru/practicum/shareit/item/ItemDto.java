package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Long request;

    @AllArgsConstructor
    static class User {
        private Long id;
        private String name;
    }
}
