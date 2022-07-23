package ru.practicum.shareit.booking;

import javax.validation.Valid;
import java.util.List;

public interface BookingService {
    List<BookingDto> getRequesterBookings(Long userId, String state);

    List<BookingDto> getOwnerBookings(Long userId, String state);

    BookingDto getBookingById(Long userId, Long bookingId);

    BookingDto addBooking(Long userId, Long itemId, @Valid BookingDtoAdd bookingDtoAdd);

    BookingDto updateBookingStatus(Long userId, Long bookingId, Boolean approved);
}
