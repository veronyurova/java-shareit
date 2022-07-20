package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    List<Booking> getRequesterBookings(Long userId, BookingState state);

    List<Booking> getOwnerBookings(Long userId, BookingState state);

    Booking getBookingById(Long userId, Long bookingId);

    Booking addBooking(Long userId, Long itemId, Booking booking);

    Booking updateBookingStatus(Long userId, Long bookingId, boolean approved);
}
