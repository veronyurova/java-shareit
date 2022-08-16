package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    List<BookingDto> getRequesterBookings(Long userId, BookingState state, int from, int size);

    List<BookingDto> getOwnerBookings(Long userId, BookingState state, int from, int size);

    BookingDto getBookingById(Long userId, Long bookingId);

    BookingDto addBooking(Long userId, BookingDtoAdd bookingDtoAdd);

    BookingDto updateBookingStatus(Long userId, Long bookingId, Boolean approved);
}
