package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;

import java.util.List;

@Service
public interface ItemService {
    List<ItemDtoWithBookingsAndComments> getItems(Integer userId);

    ItemDtoWithBookingsAndComments getItem(Integer userId);

    ItemDto addNewItem(Integer userId, ItemDto item);

    void deleteItem(Integer userId, Integer itemId);

    ItemDto changeItem(Integer userId, Integer itemId, ItemDto item);

    CommentDto addNewComment(Integer userId, Integer itemId, CommentDto newComment);

    List<ItemDto> searchItem(String textToSearch);
}
