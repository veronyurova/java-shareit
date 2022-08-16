package ru.practicum.shareit.booking;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

public interface BookingService {
    List<BookingDto> getRequesterBookings(Long userId, String state,
                                          @Min(0) int from, @Min(1) int size);

    List<BookingDto> getOwnerBookings(Long userId, String state,
                                      @Min(0) int from, @Min(1) int size);

    BookingDto getBookingById(Long userId, Long bookingId);

    BookingDto addBooking(Long userId, Long itemId, @Valid BookingDtoAdd bookingDtoAdd);

    BookingDto updateBookingStatus(Long userId, Long bookingId, Boolean approved);
}
