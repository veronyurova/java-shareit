package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Booking lastBooking;
    private Booking nextBooking;

    public void setLastBooking(ru.practicum.shareit.booking.Booking booking) {
        if (booking != null) {
            this.lastBooking = new Booking(
                    booking.getId(),
                    booking.getBooker().getId(),
                    booking.getStart(),
                    booking.getEnd()
            );
        }
    }

    public void setNextBooking(ru.practicum.shareit.booking.Booking booking) {
        if (booking != null) {
            this.nextBooking = new Booking(
                    booking.getId(),
                    booking.getBooker().getId(),
                    booking.getStart(),
                    booking.getEnd()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    static class User {
        private Long id;
        private String name;
    }

    @Getter
    @AllArgsConstructor
    static class Booking {
        private Long id;
        private Long bookerId;
        private LocalDateTime start;
        private LocalDateTime end;
    }
}
