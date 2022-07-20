package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private BookingStatus status;

    @Getter
    @AllArgsConstructor
    static class Item {
        private Long id;
        private String name;
    }

    @Getter
    @AllArgsConstructor
    static class User {
        private Long id;
        private String name;
    }
}
