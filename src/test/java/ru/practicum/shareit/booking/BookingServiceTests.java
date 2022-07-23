package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.exception.ValidationException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceTests {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingDtoAdd booking1 = new BookingDtoAdd(null,
            LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusDays(1L));
    private final BookingDtoAdd booking2 = new BookingDtoAdd(null,
            LocalDateTime.now().plusMinutes(5), LocalDateTime.now().plusDays(1L));
    private final BookingDtoAdd booking3 = new BookingDtoAdd(null,
            LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusDays(1L));
    private final UserDto user1 = new UserDto(null, "User1", "user1@yandex.ru");
    private final UserDto user2 = new UserDto(null, "User2", "user2@yandex.ru");
    private final UserDto user3 = new UserDto(null, "User3", "user3@yandex.ru");
    private final ItemDto item1 = new ItemDto(null, "Item1", "Test", true, null, null, null, null);
    private final ItemDto item2 = new ItemDto(null, "Item2", "Test", true, null, null, null, null);
    private final ItemDto item3 = new ItemDto(null, "Item3", "Test", true, null, null, null, null);

    @Test
    void getRequesterBookingsAll() {
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        itemService.addItem(3L, item1);
        itemService.addItem(3L, item2);
        itemService.addItem(3L, item3);
        bookingService.addBooking(1L, 1L, booking1);
        bookingService.addBooking(1L, 2L, booking2);
        bookingService.addBooking(2L, 3L, booking3);

        List<BookingDto> bookings = bookingService.getRequesterBookings(1L, "ALL");

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
        assertEquals(2L, bookings.get(1).getId());
    }

    @Test
    void getRequesterBookingsRejected() {
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        itemService.addItem(3L, item1);
        itemService.addItem(3L, item2);
        itemService.addItem(3L, item3);
        bookingService.addBooking(1L, 1L, booking1);
        bookingService.addBooking(1L, 2L, booking2);
        bookingService.addBooking(2L, 3L, booking3);
        bookingService.updateBookingStatus(3L, 1L, false);

        List<BookingDto> bookings = bookingService.getRequesterBookings(1L, "REJECTED");

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void getRequesterBookingsWaiting() {
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        itemService.addItem(3L, item1);
        itemService.addItem(3L, item2);
        itemService.addItem(3L, item3);
        bookingService.addBooking(1L, 1L, booking1);
        bookingService.addBooking(1L, 2L, booking2);
        bookingService.addBooking(2L, 3L, booking3);

        List<BookingDto> bookings = bookingService.getRequesterBookings(1L, "WAITING");

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
        assertEquals(2L, bookings.get(1).getId());
    }

    @Test
    void getRequesterBookingsCurrent() {
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        itemService.addItem(3L, item1);
        itemService.addItem(3L, item2);
        itemService.addItem(3L, item3);
        bookingService.addBooking(1L, 1L, booking1);
        bookingService.addBooking(1L, 2L, booking2);
        bookingService.addBooking(2L, 3L, booking3);

        List<BookingDto> bookings = bookingService.getRequesterBookings(2L, "CURRENT");

        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void getRequesterBookingsPast() {
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        itemService.addItem(3L, item1);
        itemService.addItem(3L, item2);
        itemService.addItem(3L, item3);
        bookingService.addBooking(1L, 1L, booking1);
        bookingService.addBooking(1L, 2L, booking2);
        bookingService.addBooking(2L, 3L, booking3);

        List<BookingDto> bookings = bookingService.getRequesterBookings(1L, "PAST");

        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void getRequesterBookingsFuture() {
        BookingDtoAdd booking = new BookingDtoAdd(null, LocalDateTime.now().plusDays(1L),
                LocalDateTime.now().plusDays(2L));
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        itemService.addItem(3L, item1);
        itemService.addItem(3L, item2);
        itemService.addItem(3L, item3);
        bookingService.addBooking(1L, 1L, booking);
        bookingService.addBooking(1L, 2L, booking2);
        bookingService.addBooking(2L, 3L, booking3);

        List<BookingDto> bookings = bookingService.getRequesterBookings(1L, "FUTURE");

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
        assertEquals(2L, bookings.get(1).getId());
    }

    @Test
    void getRequesterBookingsNoBookings() {
        userService.addUser(user1);

        List<BookingDto> bookings = bookingService.getRequesterBookings(1L, "ALL");

        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void getOwnerBookingsAll() {
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        itemService.addItem(1L, item1);
        itemService.addItem(1L, item2);
        itemService.addItem(2L, item3);
        bookingService.addBooking(3L, 1L, booking1);
        bookingService.addBooking(3L, 2L, booking2);
        bookingService.addBooking(3L, 3L, booking3);

        List<BookingDto> bookings = bookingService.getOwnerBookings(1L, "ALL");

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
        assertEquals(2L, bookings.get(1).getId());
    }

    @Test
    void getOwnerBookingsRejected() {
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        itemService.addItem(1L, item1);
        itemService.addItem(1L, item2);
        itemService.addItem(2L, item3);
        bookingService.addBooking(3L, 1L, booking1);
        bookingService.addBooking(3L, 2L, booking2);
        bookingService.addBooking(3L, 3L, booking3);
        bookingService.updateBookingStatus(1L, 1L, false);

        List<BookingDto> bookings = bookingService.getOwnerBookings(1L, "REJECTED");

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void getOwnerBookingsWaiting() {
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        itemService.addItem(1L, item1);
        itemService.addItem(1L, item2);
        itemService.addItem(2L, item3);
        bookingService.addBooking(3L, 1L, booking1);
        bookingService.addBooking(3L, 2L, booking2);
        bookingService.addBooking(3L, 3L, booking3);

        List<BookingDto> bookings = bookingService.getOwnerBookings(1L, "WAITING");

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
        assertEquals(2L, bookings.get(1).getId());
    }

    @Test
    void getOwnerBookingsCurrent() {
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        itemService.addItem(1L, item1);
        itemService.addItem(1L, item2);
        itemService.addItem(2L, item3);
        bookingService.addBooking(3L, 1L, booking1);
        bookingService.addBooking(3L, 2L, booking2);
        bookingService.addBooking(3L, 3L, booking3);

        List<BookingDto> bookings = bookingService.getOwnerBookings(2L, "CURRENT");

        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void getOwnerBookingsNoBookings() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);

        List<BookingDto> bookings = bookingService.getOwnerBookings(1L, "ALL");

        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void getBookingByIdByOwner() {
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);
        bookingService.addBooking(2L, 1L, booking1);

        BookingDto booking = bookingService.getBookingById(1L, 1L);

        assertNotNull(booking);
        assertEquals(1L, booking.getId());
    }

    @Test
    void getBookingByIdByBooker() {
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);
        bookingService.addBooking(2L, 1L, booking1);

        BookingDto booking = bookingService.getBookingById(2L, 1L);

        assertNotNull(booking);
        assertEquals(1L, booking.getId());
    }

    @Test
    void getBookingByIdAccessDenied() {
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        itemService.addItem(1L, item1);
        bookingService.addBooking(2L, 1L, booking1);

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.getBookingById(3L, 1L));
    }

    @Test
    void getBookingByIdNoSuchBooking() {
        userService.addUser(user1);

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    void addBooking() {
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);

        BookingDto booking = bookingService.addBooking(2L, 1L, booking1);

        assertNotNull(booking);
        assertEquals(1L, booking.getId());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
    }

    @Test
    void addBookingForOwnItem() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.addBooking(1L, 1L, booking1));
    }

    @Test
    void addBookingForUnavailableItem() {
        ItemDto newItem = new ItemDto(null, null, null, false, null, null, null, null);
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);
        itemService.updateItem(1L, 1L, newItem);

        assertThrows(ValidationException.class,
                () -> bookingService.addBooking(2L, 1L, booking1));
    }

    @Test
    void addBookingEndBeforeStart() {
        BookingDtoAdd booking = new BookingDtoAdd(null, LocalDateTime.now().plusDays(1L),
                LocalDateTime.now().plusMinutes(1));
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);

        assertThrows(ValidationException.class,
                () -> bookingService.addBooking(2L, 1L, booking));
    }

    @Test
    void addBookingNoSuchItem() {
        userService.addUser(user1);

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.addBooking(1L, 1L, booking1));
    }

    @Test
    void updateBookingStatusSetApproved() {
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);
        bookingService.addBooking(2L, 1L, booking1);

        BookingDto booking = bookingService.updateBookingStatus(1L, 1L, true);

        assertNotNull(booking);
        assertEquals(1L, booking.getId());
        assertEquals(BookingStatus.APPROVED, booking.getStatus());
    }

    @Test
    void updateBookingStatusSetRejected() {
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);
        bookingService.addBooking(2L, 1L, booking1);

        BookingDto booking = bookingService.updateBookingStatus(1L, 1L, false);

        assertNotNull(booking);
        assertEquals(1L, booking.getId());
        assertEquals(BookingStatus.REJECTED, booking.getStatus());
    }

    @Test
    void updateBookingStatusAccessDenied() {
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);
        bookingService.addBooking(2L, 1L, booking1);

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.updateBookingStatus(2L, 1L, true));
    }

    @Test
    void updateBookingStatusNoSuchBooking() {
        userService.addUser(user1);

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.updateBookingStatus(1L, 1L, true));
    }
}
