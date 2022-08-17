package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceIntegrationTest {
    private final EntityManager manager;
    private final BookingService bookingService;
    private final User user1 = new User(1L, "User 1", "user1@mail.com");
    private final User user2 = new User(2L, "User 2", "user2@mail.com");
    private final User userAdd1 = new User(null, "User 1", "user1@mail.com");
    private final User userAdd2 = new User(null, "User 2", "user2@mail.com");
    private final Item item1 = new Item(1L, "Item", "Test", true, user1, null);
    private final Item item2 = new Item(2L, "Item", "Test", true, user2, null);
    private final Item itemAdd1 = new Item(null, "Item", "Test", true, user1, null);
    private final Item itemAdd2 = new Item(null, "Item", "Test", true, user2, null);
    private final Booking bookingAdd1 = new Booking(null, LocalDateTime.MIN, LocalDateTime.MAX,
            item1, user2, BookingStatus.WAITING);
    private final Booking bookingAdd2 = new Booking(null, LocalDateTime.MIN, LocalDateTime.MAX,
            item2, user1, BookingStatus.WAITING);
    private final BookingDto bookingExpected = new BookingDto(1L, LocalDateTime.MIN,
            LocalDateTime.MAX, new BookingDto.ItemDto(1L, "Item"),
            new BookingDto.UserDto(2L, "User 2"), BookingStatus.WAITING);

    @Test
    void getRequesterBookings() {
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd1);
        manager.persist(bookingAdd2);

        List<BookingDto> bookings = bookingService.getRequesterBookings(2L, BookingState.ALL,
                0, 10);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookingExpected, bookings.get(0));
    }

    @Test
    void getOwnerBookings() {
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd1);
        manager.persist(bookingAdd2);

        List<BookingDto> bookings = bookingService.getOwnerBookings(1L, BookingState.ALL, 0, 10);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookingExpected, bookings.get(0));
    }

    @Test
    void getBookingById() {
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(bookingAdd1);

        BookingDto booking = bookingService.getBookingById(1L, 1L);

        assertNotNull(booking);
        assertEquals(bookingExpected, booking);
    }

    @Test
    void addBooking() {
        BookingDtoAdd bookingDtoAdd = new BookingDtoAdd(1L, LocalDateTime.now().plusDays(1L),
                LocalDateTime.now().plusDays(2L));
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);

        bookingService.addBooking(2L, bookingDtoAdd);

        String queryString = "SELECT b FROM Booking b WHERE b.id = 1";
        TypedQuery<Booking> query = manager.createQuery(queryString, Booking.class);
        Booking booking = query.getSingleResult();

        assertNotNull(booking);
        assertEquals(1L, booking.getId());
    }

    @Test
    void updateBookingStatus() {
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(bookingAdd1);

        bookingService.updateBookingStatus(1L, 1L, true);

        String queryString = "SELECT b FROM Booking b WHERE b.id = 1";
        TypedQuery<Booking> query = manager.createQuery(queryString, Booking.class);
        Booking booking = query.getSingleResult();

        assertNotNull(booking);
        assertEquals(BookingStatus.APPROVED, booking.getStatus());
    }
}
