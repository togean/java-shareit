package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoWithItem;

import java.util.List;

@Service
public interface BookingService {
    BookingDtoWithItem addBooking(BookingDto booking, Integer bookingId);

    BookingDtoWithItem getBookingById(Integer bookerId, Integer bookingId);

    List<BookingDtoWithItem> getBookingByUser(Integer userId, String state);

    List<BookingDtoWithItem> getBookingByOwner(Integer ownerId, String state);

    BookingDtoWithItem bookingUpdateStatus(Integer bookingId, Integer userId, Boolean bookingStatus);

}
