package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStart(Integer userId, LocalDateTime timeForStart, LocalDateTime timeForEnd);

    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStart(Integer ownerId, LocalDateTime timeForStart, LocalDateTime timeForEnd);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStart(Integer userId, LocalDateTime currentTime);

    List<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStart(Integer ownerId, LocalDateTime currentTime);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStart(Integer userId, LocalDateTime currentTime);

    List<Booking> findAllByItemOwnerIdAndStartIsAfterOrderByStart(Integer ownerId, LocalDateTime currentTime);

    List<Booking> findAllByBookerIdAndStatusOrderByStart(Integer userId, BookingStatus status);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStart(Integer ownerId, BookingStatus status);

    List<Booking> findAllByBookerIdOrderByStart(Integer userId);

    List<Booking> findAllByItemOwnerIdOrderByStart(Integer ownerId);

    List<Booking> findAllByItem_id(Integer itemId);

    List<Booking> findAllByItem_idAndStatusOrderByStartAsc(Integer itemId, BookingStatus status);
}
