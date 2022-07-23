package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private User owner;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<CommentDto> comments;

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

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    @Getter
    @AllArgsConstructor
    public static class User {
        private Long id;
        private String name;
    }

    @Getter
    @AllArgsConstructor
    public static class Booking {
        private Long id;
        private Long bookerId;
        private LocalDateTime start;
        private LocalDateTime end;
    }
}
