package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addNewItem(Integer userId, ItemDto item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> getItems(Integer userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItem(Integer userId, Integer itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> addNewComment(Integer userId, Integer itemId, CommentDto comment) {
        return post("/" + itemId + "/comment", userId, null, comment);
    }

    public ResponseEntity<Object> changeItem(Integer userId, Integer itemId, ItemDto item) {
        return patch("/" + itemId, userId, item);
    }

    public ResponseEntity<Object> searchItem(Integer userId, String textToSearch) {
        Map<String, Object> parameters = Map.of("text", textToSearch);
        return get("/search?text={text}", userId, parameters);
    }

    public void deleteItem(Integer userId, Integer itemId) {
        delete("/" + itemId, userId);
    }
}
