package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.booking.Booking;

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
    private UserDto owner;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;

    public void setLastBooking(Booking booking) {
        if (booking != null) {
            this.lastBooking = new BookingDto(
                    booking.getId(),
                    booking.getBooker().getId(),
                    booking.getStart(),
                    booking.getEnd()
            );
        }
    }

    public void setNextBooking(Booking booking) {
        if (booking != null) {
            this.nextBooking = new BookingDto(
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
    public static class UserDto {
        private Long id;
        private String name;
    }

    @Getter
    @AllArgsConstructor
    public static class BookingDto {
        private Long id;
        private Long bookerId;
        private LocalDateTime start;
        private LocalDateTime end;
    }
}
