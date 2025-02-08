package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoWithItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapper {
    public Booking toBooking(BookingDto bookingDto, Item item) {
        Booking newBooking = new Booking();
        newBooking.setBooker(bookingDto.getBooker());
        newBooking.setId(bookingDto.getId());
        newBooking.setItem(item);
        newBooking.setStatus(bookingDto.getStatus());
        newBooking.setStart(bookingDto.getStart());
        newBooking.setEnd(bookingDto.getEnd());
        return newBooking;
    }

    public BookingDto toBookingDto(Booking booking) {
        BookingDto newBookingDto = new BookingDto();
        newBookingDto.setBooker(booking.getBooker());
        newBookingDto.setId(booking.getId());
        newBookingDto.setItemId(booking.getItem().getId());
        newBookingDto.setStatus(booking.getStatus());
        newBookingDto.setStart(booking.getStart());
        newBookingDto.setEnd(booking.getEnd());
        return newBookingDto;
    }

    public BookingDtoWithItem toBookingDtoWithItem(Booking booking, Item item) {
        BookingDtoWithItem newBookingDto = new BookingDtoWithItem();
        newBookingDto.setBooker(booking.getBooker());
        newBookingDto.setId(booking.getId());
        newBookingDto.setItem(item);
        newBookingDto.setStatus(booking.getStatus());
        newBookingDto.setStart(booking.getStart());
        newBookingDto.setEnd(booking.getEnd());
        return newBookingDto;
    }

    public List<BookingDto> listOfBookingDto(List<Booking> bookingList) {
        List<BookingDto> listOfBookingDto = new ArrayList<>();
        for (Booking booking : bookingList) {
            listOfBookingDto.add(toBookingDto(booking));
        }
        return listOfBookingDto;
    }

    public List<BookingDtoWithItem> listOfBookingDtoWithItem(List<Booking> bookingList) {
        List<BookingDtoWithItem> listOfBookingDtoWithItem = new ArrayList<>();
        for (Booking booking : bookingList) {
            listOfBookingDtoWithItem.add(toBookingDtoWithItem(booking, booking.getItem()));
        }
        return listOfBookingDtoWithItem;
    }
}
