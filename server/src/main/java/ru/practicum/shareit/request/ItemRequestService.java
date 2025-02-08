package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(Integer requesterId, ItemRequestDto requestToBeAdded);
    List<ItemRequestDtoWithItem> getAllItemRequestsByRequester(Integer requesterId);
    List<ItemRequestDto> getAllItemRequests(Integer requesterId);

    ItemRequestDtoWithItem getItemRequestById(Integer requestId);
}
