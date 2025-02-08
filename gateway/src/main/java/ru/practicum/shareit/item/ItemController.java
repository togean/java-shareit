package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;
    private final String requestHeader = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(requestHeader) Integer userId) {
        log.info("ItemController: Выполнение запроса на получение вещей пользователя c ID={}", userId);
        return itemClient.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(requestHeader) Integer userId, @PathVariable(name = "itemId") Integer itemId) {
        log.info("ItemController: Выполнение запроса на получение вещи c ID={}", itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(requestHeader) Integer userId, @RequestParam String text) {
        log.info("ItemController: Выполнение запроса поиск текста \"{}\"", text);
        return itemClient.searchItem(userId, text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addItem(@RequestHeader(requestHeader) Integer userId,
                                          @RequestBody ItemDto item) {
        log.info("ItemController: Выполнение запроса добавление вещи {}", item);
        return itemClient.addNewItem(userId, item);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addNewComment(@RequestHeader(requestHeader) Integer authorId, @PathVariable Integer itemId, @RequestBody CommentDto newComment) {
        log.info("ItemController: Запрос на добавление комментария к выбранной вещи: {}", itemId);
        return itemClient.addNewComment(authorId, itemId, newComment);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> changeItem(@RequestHeader(requestHeader) Integer userId,
                              @PathVariable(name = "itemId") Integer itemId,
                              @RequestBody ItemDto item) {
        log.info("ItemController: Выполнение запроса обновление вещи с id={} на вещь: {}", itemId, item);
        return itemClient.changeItem(userId, itemId, item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(requestHeader) Integer userId,
                           @PathVariable(name = "itemId") Integer itemId) {
        log.info("ItemController: Выполнение запроса удаление вещи с ID={}", itemId);
        itemClient.deleteItem(userId, itemId);
    }
}
