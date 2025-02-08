package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDtoShortForRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemRequestMapper {
    public ItemRequest toItemRequest(ItemRequestDto itemRequest, LocalDateTime createdTime, User requester){
        ItemRequest newItemRequest = new ItemRequest();
        newItemRequest.setId(itemRequest.getId());
        newItemRequest.setDescription(itemRequest.getDescription());
        newItemRequest.setRequester(requester);
        newItemRequest.setCreated(createdTime);
        return newItemRequest;
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest){
        ItemRequestDto newItemRequestDto = new ItemRequestDto();
        newItemRequestDto.setId(itemRequest.getId());
        newItemRequestDto.setDescription(itemRequest.getDescription());
        newItemRequestDto.setCreated(itemRequest.getCreated());
        newItemRequestDto.setRequester(itemRequest.getRequester().getId());
        return newItemRequestDto;
    }

    public List<ItemRequestDto> toListOfItemRequestDto(List<ItemRequest> itemRequestsList){
        List<ItemRequestDto> newListOfItemRequestDto = new ArrayList<>();
        for(ItemRequest request: itemRequestsList){
            newListOfItemRequestDto.add(toItemRequestDto(request));
        }
        return newListOfItemRequestDto;
    }

    public ItemRequestDtoWithItem toItemRequestDtoWithItems(ItemRequest itemRequest, List<ItemDtoShortForRequest> items){
        ItemRequestDtoWithItem itemRequestDtoWithItems = new ItemRequestDtoWithItem();
        itemRequestDtoWithItems.setId(itemRequest.getId());
        itemRequestDtoWithItems.setItems(items);
        itemRequestDtoWithItems.setCreated(itemRequest.getCreated());
        itemRequestDtoWithItems.setDescription(itemRequest.getDescription());
        itemRequestDtoWithItems.setRequester(itemRequest.getRequester().getId());
        return itemRequestDtoWithItems;
    }

}
