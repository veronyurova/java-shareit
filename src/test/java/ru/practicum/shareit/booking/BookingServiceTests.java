package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.item.ItemService;
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
    private final Booking booking1 = new Booking(null, LocalDateTime.now().plusMinutes(10),
            LocalDateTime.now().plusDays(1L), null, null, null);
    private final Booking booking2 = new Booking(null, LocalDateTime.now().plusMinutes(5),
            LocalDateTime.now().plusDays(1L), null, null, null);
    private final Booking booking3 = new Booking(null, LocalDateTime.now(),
            LocalDateTime.now().plusDays(1L), null, null, null);
    private final User user1 = new User(null, "User 1", "user1@yandex.ru");
    private final User user2 = new User(null, "User 2", "user2@yandex.ru");
    private final User user3 = new User(null, "User 3", "user3@yandex.ru");
    private final Item item1 = new Item(null, "Item 1", "Test", true, null, null);
    private final Item item2 = new Item(null, "Item 2", "Test", true, null, null);
    private final Item item3 = new Item(null, "Item 3", "Test", true, null, null);

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

        List<Booking> bookings = bookingService.getRequesterBookings(1L, BookingState.ALL);

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

        List<Booking> bookings = bookingService.getRequesterBookings(1L, BookingState.REJECTED);

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

        List<Booking> bookings = bookingService.getRequesterBookings(1L, BookingState.WAITING);

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

        List<Booking> bookings = bookingService.getRequesterBookings(2L, BookingState.CURRENT);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
    }

    @Test
    void getRequesterBookingsPast() {
        Booking booking = new Booking(null, LocalDateTime.now().minusDays(2L),
                LocalDateTime.now().minusDays(1L), null, null, null);
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        itemService.addItem(3L, item1);
        itemService.addItem(3L, item2);
        itemService.addItem(3L, item3);
        bookingService.addBooking(1L, 1L, booking);
        bookingService.addBooking(1L, 2L, booking2);
        bookingService.addBooking(2L, 3L, booking3);

        List<Booking> bookings = bookingService.getRequesterBookings(1L, BookingState.PAST);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void getRequesterBookingsFuture() {
        Booking booking = new Booking(null, LocalDateTime.now().plusDays(1L),
                LocalDateTime.now().plusDays(2L), null, null, null);
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addUser(user3);
        itemService.addItem(3L, item1);
        itemService.addItem(3L, item2);
        itemService.addItem(3L, item3);
        bookingService.addBooking(1L, 1L, booking);
        bookingService.addBooking(1L, 2L, booking2);
        bookingService.addBooking(2L, 3L, booking3);

        List<Booking> bookings = bookingService.getRequesterBookings(1L, BookingState.FUTURE);

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
        assertEquals(2L, bookings.get(1).getId());
    }

    @Test
    void getRequesterBookingsNoBookings() {
        userService.addUser(user1);

        List<Booking> bookings = bookingService.getRequesterBookings(1L, BookingState.ALL);

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

        List<Booking> bookings = bookingService.getOwnerBookings(1L, BookingState.ALL);

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

        List<Booking> bookings = bookingService.getOwnerBookings(1L, BookingState.REJECTED);

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

        List<Booking> bookings = bookingService.getOwnerBookings(1L, BookingState.WAITING);

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

        List<Booking> bookings = bookingService.getOwnerBookings(2L, BookingState.CURRENT);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
    }

    @Test
    void getOwnerBookingsNoBookings() {
        userService.addUser(user1);
        itemService.addItem(1L, item1);

        List<Booking> bookings = bookingService.getOwnerBookings(1L, BookingState.ALL);

        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void getBookingByIdByOwner() {
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);
        bookingService.addBooking(2L, 1L, booking1);

        Booking booking = bookingService.getBookingById(1L, 1L);

        assertNotNull(booking);
        assertEquals(1L, booking.getId());
    }

    @Test
    void getBookingByIdByBooker() {
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);
        bookingService.addBooking(2L, 1L, booking1);

        Booking booking = bookingService.getBookingById(2L, 1L);

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

        Booking booking = bookingService.addBooking(2L, 1L, booking1);

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
        Item newItem = new Item(null, null, null, false, null, null);
        userService.addUser(user1);
        userService.addUser(user2);
        itemService.addItem(1L, item1);
        itemService.updateItem(1L, 1L, newItem);

        assertThrows(ValidationException.class,
                () -> bookingService.addBooking(2L, 1L, booking1));
    }

    @Test
    void addBookingEndBeforeStart() {
        Booking booking = new Booking(null, LocalDateTime.now().plusDays(1L),
                LocalDateTime.now().minusDays(1L), null, null, null);
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

        Booking booking = bookingService.updateBookingStatus(1L, 1L, true);

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

        Booking booking = bookingService.updateBookingStatus(1L, 1L, false);

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
