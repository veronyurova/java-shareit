package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;

    @AllArgsConstructor
    static class User {
        private Long id;
        private String name;
    }
}
