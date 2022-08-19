package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.exception.ValidationException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<BookingDto> getRequesterBookings(Long userId, String state, int from, int size) {
        BookingState bookingState = convertStringToBookingState(state);
        userService.getUserById(userId);
        List<Booking> bookings = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size);
        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(userId, pageable);
                break;
            case PAST:
                bookings = bookingRepository.findPastByBookerId(userId, LocalDateTime.now(),
                        pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findFutureByBookerId(userId, LocalDateTime.now(),
                        pageable);
                break;
            case CURRENT:
                bookings = bookingRepository.findCurrentByBookerId(userId, LocalDateTime.now(),
                        pageable);
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerIdAndStatus(userId,
                        BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStatus(userId,
                        BookingStatus.REJECTED, pageable);
                break;
        }
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getOwnerBookings(Long userId, String state, int from, int size) {
        BookingState bookingState = convertStringToBookingState(state);
        userService.getUserById(userId);
        List<Booking> bookings = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size);
        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllByOwnerId(userId, pageable);
                break;
            case PAST:
                bookings = bookingRepository.findPastByOwnerId(userId, LocalDateTime.now(),
                        pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findFutureByOwnerId(userId, LocalDateTime.now(),
                        pageable);
                break;
            case CURRENT:
                bookings = bookingRepository.findCurrentByOwnerId(userId, LocalDateTime.now(),
                        pageable);
                break;
            case WAITING:
                bookings = bookingRepository.findByOwnerIdAndStatus(userId,
                        BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository.findByOwnerIdAndStatus(userId,
                        BookingStatus.REJECTED, pageable);
                break;
        }
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        userService.getUserById(userId);
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
            log.warn("EntityNotFoundException at BookingServiceImpl.getBookingById: {}", message);
            throw new EntityNotFoundException(message);
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto addBooking(Long userId, BookingDtoAdd bookingDtoAdd) {
        Booking booking = BookingMapper.toBookingAdd(bookingDtoAdd);
        ItemDto itemDto = itemService.getItemById(userId, bookingDtoAdd.getItemId());
        validateDataForAddBooking(userId, itemDto, booking);
        booking.setItem(ItemMapper.toItem(itemDto));
        booking.setBooker(UserMapper.toUser(userService.getUserById(userId)));
        booking.setStatus(BookingStatus.WAITING);
        Booking addedBooking = bookingRepository.save(booking);
        log.info("BookingServiceImpl.addBooking: booking {} successfully added",
                 addedBooking.getId());
        return BookingMapper.toBookingDto(addedBooking);
    }

    @Override
    public BookingDto updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            String message = String.format("There is no booking with id %d", bookingId);
            log.warn("EntityNotFoundException at BookingServiceImpl.updateBookingStatus: {}",
                     message);
            throw new EntityNotFoundException(message);
        }
        Booking booking = bookingOptional.get();
        ItemDto itemDto = itemService.getItemById(userId, booking.getItem().getId());
        validateDataForStatusUpdate(userId, itemDto, booking);
        if (approved) booking.setStatus(BookingStatus.APPROVED);
        if (!approved) booking.setStatus(BookingStatus.REJECTED);
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("BookingServiceImpl.updateBookingStatus: booking {} " +
                 "status successfully updated", booking.getId());
        return BookingMapper.toBookingDto(updatedBooking);
    }

    private BookingState convertStringToBookingState(String state) {
        try {
            return BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            String message = String.format("Unknown state: %s", state);
            log.warn("ValidationException at BookingServiceImpl.convertStringToBookingState: {}",
                     message);
            throw new ValidationException(message);
        }
    }

    private void validateDataForAddBooking(Long userId, ItemDto itemDto, Booking booking) {
        if (userId.equals(itemDto.getOwner().getId())) {
            String message = String.format("User %d is not allowed to book his own item %d",
                    userId, itemDto.getId());
            log.warn("EntityNotFoundException at BookingServiceImpl.addBooking: {}", message);
            throw new EntityNotFoundException(message);
        }
        if (!itemDto.getAvailable()) {
            String message = String.format("Item %d is unavailable for booking", itemDto.getId());
            log.warn("ValidationException at BookingServiceImpl.addBooking: {}", message);
            throw new ValidationException(message);
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            String message = String.format("Booking end (%s) is before start (%s)",
                    booking.getEnd(), booking.getStart());
            log.warn("ValidationException at BookingServiceImpl.addBooking: {}", message);
            throw new ValidationException(message);
        }
    }

    private void validateDataForStatusUpdate(Long userId, ItemDto itemDto, Booking booking) {
        if (!userId.equals(itemDto.getOwner().getId())) {
            String message = String.format("User %d is not allowed to change booking %d",
                    userId, booking.getId());
            log.warn("EntityNotFoundException at BookingServiceImpl.updateBookingStatus: {}",
                    message);
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
    }
}
