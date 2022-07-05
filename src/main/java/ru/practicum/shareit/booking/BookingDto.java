package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private User booker;
    private String status;

    @AllArgsConstructor
    static class Item {
        private Long id;
        private String name;
    }

    @AllArgsConstructor
    static class User {
        private Long id;
        private String name;
    }
}
