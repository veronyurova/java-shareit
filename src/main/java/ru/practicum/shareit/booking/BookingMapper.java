package ru.practicum.shareit.booking;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new BookingDto.Item(booking.getItem().getId(), booking.getItem().getName()),
                new BookingDto.User(booking.getBooker().getId(), booking.getBooker().getName()),
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                null,
                null,
                bookingDto.getStatus()
        );
    }

    public static Booking toBookingAdd(BookingDtoAdd bookingDtoAdd) {
        return new Booking(
                null,
                bookingDtoAdd.getStart(),
                bookingDtoAdd.getEnd(),
                null,
                null,
                null
        );
    }
}
