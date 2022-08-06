package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.exception.ValidationException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private BookingService bookingService;
    @Mock
    BookingRepository mockBookingRepository;
    @Mock
    private UserService mockUserService;
    @Mock
    private ItemService mockItemService;
    private final User user = new User(1L, "User", "user@mail.com");
    private final UserDto userDto = new UserDto(1L, "User", "user@mail.com");
    private final User user2 = new User(2L, "User2", "user2@mail.com");
    private final Item item = new Item(1L, "Item 1", "Test", true, user2, null);
    private final ItemDto itemDto = new ItemDto(1L, "Item 1", "Test", true,
            new ItemDto.UserDto(2L, "User2"), null, null, null, null);
    private final Booking bookingGet = new Booking(1L, LocalDateTime.MIN, LocalDateTime.MAX,
            item, user, BookingStatus.WAITING);
    private final BookingDtoAdd bookingDtoSave = new BookingDtoAdd(null, LocalDateTime.MIN,
            LocalDateTime.MAX);
    private final BookingDto bookingExpected = new BookingDto(1L, LocalDateTime.MIN,
            LocalDateTime.MAX, new BookingDto.ItemDto(1L, "Item 1"),
            new BookingDto.UserDto(1L, "User"), BookingStatus.WAITING);

    @BeforeEach
    void beforeEach() {
        bookingService = new BookingServiceImpl(mockBookingRepository, mockUserService,
                mockItemService);
    }

    @Test
    void getRequesterBookingsAll() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findAllByBookerId(
                        Mockito.anyLong(),
                        Mockito.any(Pageable.class)
                        )
                )
                .thenReturn(List.of(bookingGet));

        List<BookingDto> bookings = bookingService.getRequesterBookings(1L, "ALL", 0 , 10);

        assertNotNull(bookings);
        assertEquals(bookingExpected, bookings.get(0));
    }

    @Test
    void getRequesterBookingsPast() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findPastByBookerId(
                                Mockito.anyLong(),
                                Mockito.any(LocalDateTime.class),
                                Mockito.any(Pageable.class)
                        )
                )
                .thenReturn(List.of(bookingGet));

        List<BookingDto> bookings = bookingService.getRequesterBookings(1L, "PAST", 0 , 10);

        assertNotNull(bookings);
        assertEquals(bookingExpected, bookings.get(0));
    }

    @Test
    void getRequesterBookingsFuture() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findFutureByBookerId(
                                Mockito.anyLong(),
                                Mockito.any(LocalDateTime.class),
                                Mockito.any(Pageable.class)
                        )
                )
                .thenReturn(List.of(bookingGet));

        List<BookingDto> bookings = bookingService.getRequesterBookings(1L, "FUTURE", 0 , 10);

        assertNotNull(bookings);
        assertEquals(bookingExpected, bookings.get(0));
    }

    @Test
    void getRequesterBookingsCurrent() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findCurrentByBookerId(
                                Mockito.anyLong(),
                                Mockito.any(LocalDateTime.class),
                                Mockito.any(Pageable.class)
                        )
                )
                .thenReturn(List.of(bookingGet));

        List<BookingDto> bookings = bookingService.getRequesterBookings(1L, "CURRENT", 0 , 10);

        assertNotNull(bookings);
        assertEquals(bookingExpected, bookings.get(0));
    }

    @Test
    void getRequesterBookingsWaiting() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findByBookerIdAndStatus(
                                Mockito.anyLong(),
                                Mockito.any(BookingStatus.class),
                                Mockito.any(Pageable.class)
                        )
                )
                .thenReturn(List.of(bookingGet));

        List<BookingDto> bookings = bookingService.getRequesterBookings(1L, "WAITING", 0 , 10);

        assertNotNull(bookings);
        assertEquals(bookingExpected, bookings.get(0));
    }

    @Test
    void getRequesterBookingsRejected() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findByBookerIdAndStatus(
                                Mockito.anyLong(),
                                Mockito.any(BookingStatus.class),
                                Mockito.any(Pageable.class)
                        )
                )
                .thenReturn(List.of(bookingGet));

        List<BookingDto> bookings = bookingService.getRequesterBookings(1L, "REJECTED", 0 , 10);

        assertNotNull(bookings);
        assertEquals(bookingExpected, bookings.get(0));
    }

    @Test
    void getRequesterBookingsIncorrectState() {
        assertThrows(ValidationException.class,
                () -> bookingService.getRequesterBookings(1L, "INCORRECT", 0 , 10));
    }

    @Test
    void getOwnerBookingsAll() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findAllByOwnerId(
                                Mockito.anyLong(),
                                Mockito.any(Pageable.class)
                        )
                )
                .thenReturn(List.of(bookingGet));

        List<BookingDto> bookings = bookingService.getOwnerBookings(2L, "ALL", 0 , 10);

        assertNotNull(bookings);
        assertEquals(bookingExpected, bookings.get(0));
    }

    @Test
    void getOwnerBookingsPast() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findPastByOwnerId(
                                Mockito.anyLong(),
                                Mockito.any(LocalDateTime.class),
                                Mockito.any(Pageable.class)
                        )
                )
                .thenReturn(List.of(bookingGet));

        List<BookingDto> bookings = bookingService.getOwnerBookings(2L, "PAST", 0 , 10);

        assertNotNull(bookings);
        assertEquals(bookingExpected, bookings.get(0));
    }

    @Test
    void getOwnerBookingsFuture() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findFutureByOwnerId(
                                Mockito.anyLong(),
                                Mockito.any(LocalDateTime.class),
                                Mockito.any(Pageable.class)
                        )
                )
                .thenReturn(List.of(bookingGet));

        List<BookingDto> bookings = bookingService.getOwnerBookings(2L, "FUTURE", 0 , 10);

        assertNotNull(bookings);
        assertEquals(bookingExpected, bookings.get(0));
    }

    @Test
    void getOwnerBookingsCurrent() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findCurrentByOwnerId(
                                Mockito.anyLong(),
                                Mockito.any(LocalDateTime.class),
                                Mockito.any(Pageable.class)
                        )
                )
                .thenReturn(List.of(bookingGet));

        List<BookingDto> bookings = bookingService.getOwnerBookings(2L, "CURRENT", 0 , 10);

        assertNotNull(bookings);
        assertEquals(bookingExpected, bookings.get(0));
    }

    @Test
    void getOwnerBookingsWaiting() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findByOwnerIdAndStatus(
                                Mockito.anyLong(),
                                Mockito.any(BookingStatus.class),
                                Mockito.any(Pageable.class)
                        )
                )
                .thenReturn(List.of(bookingGet));

        List<BookingDto> bookings = bookingService.getOwnerBookings(2L, "WAITING", 0 , 10);

        assertNotNull(bookings);
        assertEquals(bookingExpected, bookings.get(0));
    }

    @Test
    void getOwnerBookingsRejected() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findByOwnerIdAndStatus(
                                Mockito.anyLong(),
                                Mockito.any(BookingStatus.class),
                                Mockito.any(Pageable.class)
                        )
                )
                .thenReturn(List.of(bookingGet));

        List<BookingDto> bookings = bookingService.getOwnerBookings(2L, "REJECTED", 0 , 10);

        assertNotNull(bookings);
        assertEquals(bookingExpected, bookings.get(0));
    }

    @Test
    void getOwnerBookingsIncorrectState() {
        assertThrows(ValidationException.class, () -> bookingService.getOwnerBookings(1L,
                "INCORRECT", 0 , 10));
    }

    @Test
    void getBookingByIdByOwner() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(bookingGet));

        BookingDto booking = bookingService.getBookingById(2L, 1L);

        assertNotNull(booking);
        assertEquals(bookingExpected, booking);
    }

    @Test
    void getBookingByIdByRequester() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(bookingGet));

        BookingDto booking = bookingService.getBookingById(1L, 1L);

        assertNotNull(booking);
        assertEquals(bookingExpected, booking);
    }

    @Test
    void getBookingByIdIncorrectUserId() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(bookingGet));

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingById(3L, 1L));
    }

    @Test
    void getBookingByIdNoSuchBooking() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    void addBooking() {
        Mockito
                .when(mockItemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(userDto);
        Mockito
                .when(mockBookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(bookingGet);

        BookingDto booking = bookingService.addBooking(1L, 1L, bookingDtoSave);

        assertNotNull(booking);
        assertEquals(bookingExpected, booking);
    }

    @Test
    void addBookingForOwnItem() {
        Mockito
                .when(mockItemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.addBooking(2L, 1L, bookingDtoSave));
    }

    @Test
    void addBookingForUnavailableItem() {
        ItemDto itemDtoUnavailable = new ItemDto(1L, "Item 1", "Test", false,
                new ItemDto.UserDto(2L, "User2"), null, null, null, null);

        Mockito
                .when(mockItemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDtoUnavailable);

        assertThrows(ValidationException.class, () -> bookingService.addBooking(1L, 1L,
                bookingDtoSave));
    }

    @Test
    void addBookingEndBeforeStart() {
        BookingDtoAdd bookingDtoAdd = new BookingDtoAdd(null, LocalDateTime.MAX,
                LocalDateTime.MIN);

        Mockito
                .when(mockItemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);

        assertThrows(ValidationException.class,
                () -> bookingService.addBooking(1L, 1L, bookingDtoAdd));
    }

    @Test
    void updateBookingStatusSetApproved() {
        Booking bookingUpd = new Booking(1L, LocalDateTime.MIN, LocalDateTime.MAX,
                item, user, BookingStatus.APPROVED);
        BookingDto bookingExpectedUpd = new BookingDto(1L, LocalDateTime.MIN,
                LocalDateTime.MAX, new BookingDto.ItemDto(1L, "Item 1"),
                new BookingDto.UserDto(1L, "User"), BookingStatus.APPROVED);

        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(bookingGet));
        Mockito
                .when(mockItemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);
        Mockito.when(mockBookingRepository.save(bookingUpd)).thenReturn(bookingUpd);

        BookingDto booking = bookingService.updateBookingStatus(2L, 1L, true);

        assertNotNull(booking);
        assertEquals(bookingExpectedUpd, booking);
    }

    @Test
    void updateBookingStatusSetRejected() {
        Booking bookingUpd = new Booking(1L, LocalDateTime.MIN, LocalDateTime.MAX,
                item, user, BookingStatus.REJECTED);
        BookingDto bookingExpectedUpd = new BookingDto(1L, LocalDateTime.MIN,
                LocalDateTime.MAX, new BookingDto.ItemDto(1L, "Item 1"),
                new BookingDto.UserDto(1L, "User"), BookingStatus.REJECTED);

        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(bookingGet));
        Mockito
                .when(mockItemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);
        Mockito.when(mockBookingRepository.save(bookingUpd)).thenReturn(bookingUpd);

        BookingDto booking = bookingService.updateBookingStatus(2L, 1L, false);

        assertNotNull(booking);
        assertEquals(bookingExpectedUpd, booking);
    }

    @Test
    void updateBookingStatusNoSuchBooking() {
        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.updateBookingStatus(1L, 1L, true));
    }

    @Test
    void updateBookingStatusAccessDenied() {
        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(bookingGet));
        Mockito
                .when(mockItemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.updateBookingStatus(1L, 1L, true));
    }

    @Test
    void updateBookingStatusAlreadyApproved() {
        Booking bookingApproved = new Booking(1L, LocalDateTime.MIN, LocalDateTime.MAX,
                item, user, BookingStatus.APPROVED);

        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(bookingApproved));
        Mockito
                .when(mockItemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);

        assertThrows(ValidationException.class,
                () -> bookingService.updateBookingStatus(2L, 1L, false));
    }

    @Test
    void updateBookingStatusAlreadyRejected() {
        Booking bookingApproved = new Booking(1L, LocalDateTime.MIN, LocalDateTime.MAX,
                item, user, BookingStatus.REJECTED);

        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(bookingApproved));
        Mockito
                .when(mockItemService.getItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);

        assertThrows(ValidationException.class,
                () -> bookingService.updateBookingStatus(2L, 1L, true));
    }
}
