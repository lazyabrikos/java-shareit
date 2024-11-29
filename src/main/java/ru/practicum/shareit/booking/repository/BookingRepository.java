package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_IdOrderByStartAsc(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.start <= ?2 " +
            "AND b.end >=?2 " +
            "ORDER BY b.start")
    List<Booking> findAllBookerCurrentBookings(Long bookerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.start <= ?2 " +
            "AND b.end <=?2 " +
            "ORDER BY b.start")
    List<Booking> findAllBookerPastBookings(Long userId, LocalDateTime now);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.start >= ?2 " +
            "AND b.end >=?2 " +
            "ORDER BY b.start")
    List<Booking> findAllBookerFutureBookings(Long userId, LocalDateTime now);

    List<Booking> findAllByBooker_IdAndStatusOrderByStartAsc(Long userId, BookingStatus status);

    List<Booking> findAllByItem_IdInOrderByStartAsc(List<Long> itemIds);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id IN ?1 " +
            "AND b.start <= ?2 " +
            "AND b.end >=?2 " +
            "ORDER BY b.start")
    List<Booking> findAllOwnerCurrentBookings(List<Long> itemIds, LocalDateTime now);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id IN ?1 " +
            "AND b.start <= ?2 " +
            "AND b.end <=?2 " +
            "ORDER BY b.start")
    List<Booking> findAllOwnerPastBookings(List<Long> itemIds, LocalDateTime now);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id IN ?1 " +
            "AND b.start >= ?2 " +
            "AND b.end >=?2 " +
            "ORDER BY b.start")
    List<Booking> findAllOwnerFutureBookings(List<Long> itemIds, LocalDateTime now);

    List<Booking> findAllByItem_IdAndBooker_IdAndStatus(Long itemId, Long bookerId, BookingStatus status);

    List<Booking> findAllByItem_IdInAndStatusOrderByStartAsc(List<Long> itemIds, BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item i " +
            "WHERE i.owner.id = ?1 " +
            "AND i.id = ?2 " +
            "AND b.end <= ?3 " +
            "AND b.status = 'APPROVED' " +
            "ORDER by b.end DESC " +
            "LIMIT 1")
    Optional<Booking> findLastBookingForItem(Long userId, Long itemId, LocalDateTime now);


    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.item i " +
            "WHERE i.owner.id = ?1 " +
            "AND i.id = ?2 " +
            "AND b.start > ?3 " +
            "AND b.status = 'APPROVED' " +
            "ORDER by b.start ASC " +
            "LIMIT 1")
    Optional<Booking> findNextBookingForItem(Long userId, Long itemId, LocalDateTime now);
}
