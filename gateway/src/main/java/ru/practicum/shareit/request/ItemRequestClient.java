package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";
    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addItemRequest(Integer requesterId, ItemRequestDto newRequest){
        return post("", requesterId, newRequest);
    }

    public ResponseEntity<Object> getAllItemRequestsByRequester(Integer requesterId){
        return get("", requesterId);
    }

    public ResponseEntity<Object> getItemRequestById(Integer requestId){
        return get("/" + requestId, requestId);
    }

    public ResponseEntity<Object> getAllItemRequests(Integer requesterId){
        return get("", requesterId);
    }
}
