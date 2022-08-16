package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager manager;
    @Autowired
    private BookingRepository repository;
    private final User user1 = new User(1L, "User 1", "user1@mail.com");
    private final User user2 = new User(2L, "User 2", "user2@mail.com");
    private final User userAdd1 = new User(null, "User 1", "user1@mail.com");
    private final User userAdd2 = new User(null, "User 2", "user2@mail.com");
    private final Item item1 = new Item(1L, "Item 1", "Test", true, user1, null);
    private final Item item2 = new Item(2L, "Item 2", "Test", true, user2, null);
    private final Item itemAdd1 = new Item(null, "Item 1", "Test", true, user1, null);
    private final Item itemAdd2 = new Item(null, "Item 2", "Test", true, user2, null);
    private final Booking bookingAdd1 = new Booking(null, LocalDateTime.MIN, LocalDateTime.MAX,
            item1, user2, BookingStatus.WAITING);
    private final Booking bookingAdd2 = new Booking(null, LocalDateTime.MIN, LocalDateTime.MAX,
            item2, user1, BookingStatus.WAITING);
    private final Pageable page = PageRequest.of(0, 10);

    @Test
    void findAllByBookerId() {
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd1);
        manager.persist(bookingAdd2);

        List<Booking> bookings = repository.findAllByBookerId(2L, page);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findByBookerIdAndStatus() {
        Booking bookingAdd = new Booking(null, LocalDateTime.MIN, LocalDateTime.MAX,
                item1, user2, BookingStatus.APPROVED);
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd);
        manager.persist(bookingAdd2);

        List<Booking> bookings = repository.findByBookerIdAndStatus(2L,
                BookingStatus.APPROVED, page);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findPastByBookerId() {
        Booking bookingAdd = new Booking(null, LocalDateTime.now().minusDays(2L),
                LocalDateTime.now().minusDays(1L), item1, user2, BookingStatus.APPROVED);
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd);
        manager.persist(bookingAdd2);

        List<Booking> bookings = repository.findPastByBookerId(2L, LocalDateTime.now(), page);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findFutureByBookerId() {
        Booking bookingAdd = new Booking(null, LocalDateTime.now().plusDays(1L),
                LocalDateTime.now().plusDays(2L), item1, user2, BookingStatus.APPROVED);
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd);
        manager.persist(bookingAdd2);

        List<Booking> bookings = repository.findFutureByBookerId(2L, LocalDateTime.now(), page);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findCurrentByBookerId() {
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd1);
        manager.persist(bookingAdd2);

        List<Booking> bookings = repository.findAllByBookerId(2L, page);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findAllByOwnerId() {
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd1);
        manager.persist(bookingAdd2);

        List<Booking> bookings = repository.findAllByOwnerId(1L, page);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findByOwnerIdAndStatus() {
        Booking bookingAdd = new Booking(null, LocalDateTime.MIN, LocalDateTime.MAX,
                item1, user2, BookingStatus.APPROVED);
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd);
        manager.persist(bookingAdd2);

        List<Booking> bookings = repository.findByOwnerIdAndStatus(1L,
                BookingStatus.APPROVED, page);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findPastByOwnerId() {
        Booking bookingAdd = new Booking(null, LocalDateTime.now().minusDays(2L),
                LocalDateTime.now().minusDays(1L), item1, user2, BookingStatus.APPROVED);
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd);
        manager.persist(bookingAdd2);

        List<Booking> bookings = repository.findPastByOwnerId(1L, LocalDateTime.now(), page);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findFutureByOwnerId() {
        Booking bookingAdd = new Booking(null, LocalDateTime.now().plusDays(1L),
                LocalDateTime.now().plusDays(2L), item1, user2, BookingStatus.APPROVED);
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd);
        manager.persist(bookingAdd2);

        List<Booking> bookings = repository.findFutureByOwnerId(1L, LocalDateTime.now(), page);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findCurrentByOwnerId() {
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd1);
        manager.persist(bookingAdd2);

        List<Booking> bookings = repository.findAllByOwnerId(1L, page);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findCompletedBooking() {
        Booking bookingAdd = new Booking(null, LocalDateTime.now().minusDays(2L),
                LocalDateTime.now().minusDays(1L), item1, user2, BookingStatus.APPROVED);
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd);
        manager.persist(bookingAdd2);

        Booking booking = repository.findCompletedBooking(2L, 1L, BookingStatus.APPROVED,
                LocalDateTime.now());

        assertNotNull(booking);
        assertEquals(1L, booking.getId());
    }

    @Test
    void findLastBookings() {
        Booking bookingAdd = new Booking(null, LocalDateTime.now().minusDays(2L),
                LocalDateTime.now().minusDays(1L), item1, user2, BookingStatus.APPROVED);
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd);
        manager.persist(bookingAdd2);

        List<Booking> bookings = repository.findLastBookings(1L, LocalDateTime.now());

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findNextBookings() {
        Booking bookingAdd = new Booking(null, LocalDateTime.now().plusDays(1L),
                LocalDateTime.now().plusDays(2L), item1, user2, BookingStatus.APPROVED);
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemAdd1);
        manager.persist(itemAdd2);
        manager.persist(bookingAdd);
        manager.persist(bookingAdd2);

        List<Booking> bookings = repository.findNextBookings(1L, LocalDateTime.now());

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }
}
