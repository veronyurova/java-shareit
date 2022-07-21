package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
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
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(
                        userId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(
                        userId, LocalDateTime.now());
            case CURRENT:
                return bookingRepository
                        .findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                                userId, LocalDateTime.now(), LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                        userId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(
                        userId, BookingStatus.REJECTED);
            default:
                String message = String.format("Incorrect selection criteria %s", state);
                log.warn("ValidationException at BookingServiceImpl.getRequesterBookings: {}",
                         message);
                throw new ValidationException(message);
        }
    }

    @Override
    public List<Booking> getOwnerBookings(Long userId, BookingState state) {
        switch (state) {
            case ALL:
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(
                        userId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(
                        userId, LocalDateTime.now());
            case CURRENT:
                return bookingRepository
                        .findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                                userId, LocalDateTime.now(), LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                        userId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                        userId, BookingStatus.REJECTED);
            default:
                String message = String.format("Incorrect search parameter %s", state);
                log.warn("ValidationException at BookingServiceImpl.getOwnerBookings: {}",
                         message);
                throw new ValidationException(message);
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
    public Booking addBooking(Long userId, Long itemId, Booking booking) {
        Item item = itemService.getItemById(itemId);
        if (userId.equals(item.getOwner().getId())) {
            String message = String.format("User %d is not allowed to book his own item %d",
                                           userId, item.getId());
            log.warn("EntityNotFoundException at BookingServiceImpl.addBooking: {}", message);
            throw new EntityNotFoundException(message);
        }
        if (!item.getAvailable()) {
            String message = String.format("Item %d is unavailable for booking", item.getId());
            log.warn("ValidationException at BookingServiceImpl.addBooking: {}", message);
            throw new ValidationException(message);
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            String message = String.format("Booking end (%s) is before start (%s)",
                                           booking.getEnd(), booking.getStart());
            log.warn("ValidationException at BookingServiceImpl.addBooking: {}", message);
            throw new ValidationException(message);
        }
        booking.setItem(item);
        booking.setBooker(userService.getUserById(userId));
        booking.setStatus(BookingStatus.WAITING);
        Booking addedBooking = bookingRepository.save(booking);
        log.info("BookingServiceImpl.addBooking: booking {} successfully added",
                 addedBooking.getId());
        return addedBooking;
    }

    @Override
    public Booking updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        Booking booking = getBookingById(userId, bookingId);
        if (!userId.equals(booking.getItem().getOwner().getId())) {
            String message = String.format("User %d is not allowed to change booking %d",
                                           userId, booking.getId());
            log.warn("EntityNotFoundException at BookingServiceImpl.updateBookingStatus: " +
                     "{}", message);
            throw new EntityNotFoundException(message);
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            String message = String.format("Booking %d is already approved", booking.getId());
            log.warn("ValidationException at BookingServiceImpl.updateBookingStatus: {}", message);
            throw new ValidationException(message);
        }
        if (booking.getStatus().equals(BookingStatus.REJECTED)) {
            String message = String.format("Booking %d is already rejected", booking.getId());
            log.warn("ValidationException at BookingServiceImpl.updateBookingStatus: {}", message);
            throw new ValidationException(message);
        }
        if (approved) booking.setStatus(BookingStatus.APPROVED);
        if (!approved) booking.setStatus(BookingStatus.REJECTED);
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("BookingServiceImpl.updateBookingStatus: booking {} " +
                 "status successfully updated", booking.getId());
        return updatedBooking;
    }
}
