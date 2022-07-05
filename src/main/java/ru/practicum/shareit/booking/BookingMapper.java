package ru.practicum.shareit.booking;

public class BookingMapper {
    public static BookingDto toItemDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new BookingDto.Item(booking.getItem().getId(), booking.getItem().getName()),
                new BookingDto.User(booking.getBooker().getId(), booking.getBooker().getName()),
                booking.getStatus()
        );
    }
}
