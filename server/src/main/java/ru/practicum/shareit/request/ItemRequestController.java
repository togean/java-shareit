package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final String requestHeader = "X-Sharer-User-Id";
    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader(requestHeader) Integer requesterId, @RequestBody ItemRequestDto newRequest){
        log.info("ItemRequestController: Выполнение запроса на создание нового реквеста на вещь пользователем с ID={}",requesterId);
        return itemRequestService.addItemRequest(requesterId, newRequest);
    }

    @GetMapping
    public List<ItemRequestDtoWithItem> getAllItemRequestsByRequester(@RequestHeader(requestHeader) Integer requesterId){
        log.info("ItemRequestController: Выполнение запроса на вывод всех реквестов пользователя c ID={}",requesterId);
        return itemRequestService.getAllItemRequestsByRequester(requesterId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoWithItem getItemRequestById(@RequestHeader(requestHeader) Integer requesterId, @PathVariable("requestId") Integer requestId){
        log.info("ItemRequestController: Выполнение запроса на вывод информации по реквесту");
        return itemRequestService.getItemRequestById(requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader(requestHeader) Integer requesterId){
        log.info("ItemRequestController: Выполнение запроса на вывод всех реквестов");
        return itemRequestService.getAllItemRequests(requesterId);
    }

}
