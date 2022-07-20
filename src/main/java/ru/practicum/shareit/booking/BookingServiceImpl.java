package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.item.ItemService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Validated
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserService userService, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public List<Booking> getRequesterBookings(Long userId, BookingState state) {
        switch (state) {
            case All:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now());
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            default:
                throw new ValidationException("No such search parameter");
        }
    }

    @Override
    public List<Booking> getOwnerBookings(Long userId, BookingState state) {
        switch (state) {
            case All:
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now());
            case CURRENT:
                return bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            default:
                throw new ValidationException("No such search parameter");
        }
    }

    @Override
    public Booking getBookingById(Long userId, Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            String message = String.format("There is no booking with id %d", bookingId);
            log.warn("EntityNotFoundException at BookingServiceImpl.getBookingById: {}", message);
            throw new EntityNotFoundException(message);
        }
        Booking booking = bookingOptional.get();
        if (!userId.equals(booking.getItem().getOwner().getId()) &&
                !userId.equals(booking.getBooker().getId())) {
            String message = String.format("User %d is not allowed to view booking %d",
                                           userId, booking.getId());
            log.warn("EntityNotFoundException at BookingServiceImpl.getBookingById: " +
                     "{}", message);
            throw new EntityNotFoundException(message);
        }
        return booking;
    }

    @Override
    public Booking addBooking(Long userId, Long itemId, @Valid Booking booking) {
        booking.setItem(itemService.getItemById(itemId));
        booking.setBooker(userService.getUserById(userId));
        booking.setStatus(BookingStatus.WAITING);
        Booking addedBooking = bookingRepository.save(booking);
        log.info("BookingServiceImpl.addItem: booking {} successfully added", booking.getId());
        return addedBooking;
    }

    @Override
    public Booking updateBookingStatus(Long userId, Long bookingId, boolean approved) {
        Booking booking = getBookingById(userId, bookingId);
        if (!userId.equals(booking.getItem().getOwner().getId())) {
            String message = String.format("User %d is not allowed to change booking %d",
                                           userId, booking.getId());
            log.warn("EntityNotFoundException at BookingServiceImpl.updateBookingStatus: " +
                     "{}", message);
            throw new EntityNotFoundException(message);
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("BookingServiceImpl.updateBookingStatus: booking {} " +
                 "status successfully updated", booking.getId());
        return updatedBooking;
    }
}
