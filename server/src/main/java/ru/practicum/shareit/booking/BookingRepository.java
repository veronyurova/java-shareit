package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.booker.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerId(Long userId, Pageable pageable);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.booker.id = ?1 AND " +
            "      b.status = ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findByBookerIdAndStatus(Long userId, BookingStatus status, Pageable pageable);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.booker.id = ?1 AND " +
            "      b.end < ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findPastByBookerId(Long userId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.booker.id = ?1 AND " +
            "      b.start > ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findFutureByBookerId(Long userId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.booker.id = ?1 AND " +
            "      b.start < ?2 AND " +
            "      b.end > ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentByBookerId(Long userId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.item.owner.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerId(Long userId, Pageable pageable);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.item.owner.id = ?1 AND " +
            "      b.status = ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findByOwnerIdAndStatus(Long userId, BookingStatus status, Pageable pageable);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.item.owner.id = ?1 AND " +
            "      b.end < ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findPastByOwnerId(Long userId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.item.owner.id = ?1 AND " +
            "      b.start > ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findFutureByOwnerId(Long userId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.item.owner.id = ?1 AND " +
            "      b.start < ?2 AND " +
            "      b.end > ?2 " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentByOwnerId(Long userId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.booker.id = ?1 AND " +
            "      b.item.id = ?2 AND " +
            "      b.status = ?3 AND " +
            "      b.end < ?4")
    Booking findCompletedBooking(Long userId, Long itemId, BookingStatus status,
                                 LocalDateTime now);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.item.id = ?1 AND " +
            "      b.end < ?2 " +
            "ORDER BY b.end DESC")
    List<Booking> findLastBookings(Long itemId, LocalDateTime end);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.item.id = ?1 AND " +
            "      b.start > ?2 " +
            "ORDER BY b.start ASC")
    List<Booking> findNextBookings(Long itemId, LocalDateTime start);
}
