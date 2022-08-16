package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    static class ItemDto {
        private Long id;
        private String name;
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    static class UserDto {
        private Long id;
        private String name;
    }
}
