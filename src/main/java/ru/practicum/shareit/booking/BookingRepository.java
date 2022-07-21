package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, BookingStatus status);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(Long userId,
                                                                  LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(Long userId,
                                                                   LocalDateTime start);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId,
            LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, BookingStatus status);

    List<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long userId,
                                                                     LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long userId,
                                                                      LocalDateTime start);

    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId,
            LocalDateTime start, LocalDateTime end);

    Booking findFirstByItemIdAndEndIsBeforeOrderByEndDesc(Long itemId, LocalDateTime end);

    Booking findFirstByItemIdAndStartIsAfterOrderByStartAsc(Long itemId, LocalDateTime start);
}
