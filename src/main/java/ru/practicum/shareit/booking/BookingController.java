package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.convert.ConversionFailedException;
import ru.practicum.shareit.exception.ValidationException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingDto> getRequesterBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(defaultValue = "ALL")
                                                 String state) {
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            String message = String.format("Incorrect selection criteria %s", state);
            log.warn("ValidationException at BookingController.getRequesterBookings: {}", message);
            throw new ValidationException(message);
        }
        return bookingService.getRequesterBookings(userId, bookingState)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(defaultValue = "ALL")
                                             String state) {
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (ConversionFailedException e) {
            String message = String.format("Incorrect selection criteria %s", state);
            log.warn("ValidationException at BookingController.getOwnerBookings: {}", message);
            throw new ValidationException(message);
        }
        return bookingService.getOwnerBookings(userId, bookingState)
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId) {
        return BookingMapper.toBookingDto(bookingService.getBookingById(userId, bookingId));
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody BookingDtoAdd bookingDtoAdd) {
        Long itemId = bookingDtoAdd.getItemId();
        Booking booking = BookingMapper.toBooking(bookingDtoAdd);
        return BookingMapper.toBookingDto(bookingService.addBooking(userId, itemId, booking));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam Boolean approved) {
        return BookingMapper.toBookingDto(bookingService.updateBookingStatus(userId, bookingId,
                approved));
    }
}
