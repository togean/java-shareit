package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addBooking(Integer userId, BookingDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> bookingUpdateStatus(Integer userId, Integer bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> getBookingById(Integer userId, Integer bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingByOwner(Integer userId, BookingStatus status) {
        Map<String, Object> parameters = Map.of("state", status.name());
        return get("/owner?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingByUser(Integer userId, String status) {
        Map<String, Object> parameters = Map.of("state", status);
        return get("?state={state}", userId, parameters);
    }
}
