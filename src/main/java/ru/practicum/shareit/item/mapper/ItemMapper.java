package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.model.Item;
import java.util.List;

@Component
public class ItemMapper {
    public ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequest(item.getRequest() != null ? item.getRequest() : null);
        itemDto.setId(item.getId());
        return itemDto;
    }

    public Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setRequest(itemDto.getRequest() != null ? itemDto.getRequest() : null);
        item.setId(itemDto.getId());
        return item;
    }

    public ItemDtoWithBookingsAndComments itemToItemDtoWithBookingsAndComments(Item item, BookingDto lastBooking, BookingDto nextBooking, List<CommentDto> comments) {
        ItemDtoWithBookingsAndComments newItem = new ItemDtoWithBookingsAndComments();
        newItem.setComments(comments);
        newItem.setLastBooking(lastBooking);
        newItem.setNextBooking(nextBooking);
        newItem.setOwner(item.getOwner());
        newItem.setDescription(item.getDescription());
        newItem.setAvailable(item.getAvailable());
        newItem.setId(item.getId());
        newItem.setName(item.getName());
        return newItem;
    }

    public ItemDtoWithBookingsAndComments itemToItemDtoWithoutBookingsOrComments(Item item, List<CommentDto> comments) {
        ItemDtoWithBookingsAndComments newItem = new ItemDtoWithBookingsAndComments();
        newItem.setOwner(item.getOwner());
        newItem.setDescription(item.getDescription());
        newItem.setAvailable(item.getAvailable());
        newItem.setId(item.getId());
        newItem.setName(item.getName());
        newItem.setLastBooking(null);
        newItem.setNextBooking(null);
        newItem.setComments(comments);
        return newItem;
    }
}
