package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return bookingService.getRequesterBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(defaultValue = "ALL")
                                             String state) {
        return bookingService.getOwnerBookings(userId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody BookingDtoAdd bookingDtoAdd) {
        return bookingService.addBooking(userId, bookingDtoAdd.getItemId(), bookingDtoAdd);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam Boolean approved) {
        return bookingService.updateBookingStatus(userId, bookingId, approved);
    }
}
