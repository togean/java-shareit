package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.model.User;
import java.util.List;

@Data
public class ItemDtoWithBookingsAndComments {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Integer request;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;
    private User owner;
}
