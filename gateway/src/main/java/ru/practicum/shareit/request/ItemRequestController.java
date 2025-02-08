package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    private final String requestHeader = "X-Sharer-User-Id";
    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(requestHeader) Integer requesterId, @RequestBody ItemRequestDto newRequest){
        log.info("ItemRequestController: Выполнение запроса на создание нового реквеста на вещь");
        return itemRequestClient.addItemRequest(requesterId, newRequest);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestsByRequester(@RequestHeader(requestHeader) Integer requesterId){
        log.info("ItemRequestController: Выполнение запроса на вывод всех реквестов пользователя");
        return itemRequestClient.getAllItemRequestsByRequester(requesterId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(requestHeader) Integer requesterId, @PathVariable("requestId") Integer requestId){
        log.info("ItemRequestController: Выполнение запроса на вывод информации по реквесту");
        return itemRequestClient.getItemRequestById(requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(requestHeader) Integer requesterId){
        log.info("ItemRequestController: Выполнение запроса на вывод всех реквестов");
        return itemRequestClient.getAllItemRequests(requesterId);
    }
}
