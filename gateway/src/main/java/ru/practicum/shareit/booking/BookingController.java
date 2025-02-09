package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatus;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingClient bookingClient;
    private final String requestHeader = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(requestHeader) Integer bookingId, @RequestBody BookingDto bookingDto) {
        log.info("BookingController: Запрос на добавление бронирования");
        return bookingClient.addBooking(bookingId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> bookingUpdateStatus(@RequestHeader(requestHeader) Integer userId, @PathVariable Integer bookingId, @RequestParam Boolean approved) {
        log.info("BookingController: Запрос на обновление статуса у бронирования с ID={}", bookingId);
        return bookingClient.bookingUpdateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(requestHeader) Integer bookerId, @PathVariable Integer bookingId) {
        log.info("BookingController: Запрос на получение данных о бронировании с ID={}", bookingId);
        return bookingClient.getBookingById(bookerId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwner(@RequestHeader(requestHeader) Integer ownerId, @RequestParam(defaultValue = "ALL") String state) {
        log.info("BookingController: Запрос на получие всех бронирований для вещей пользователя с ID={}", ownerId);
        BookingStatus status = BookingStatus.from(state).orElseThrow(() -> new IllegalArgumentException("Такой статус у брони не предусмотрен:" + state));
        return bookingClient.getBookingByOwner(ownerId, status);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingByUser(@RequestHeader(requestHeader) Integer userId, @RequestParam(defaultValue = "ALL") String state) {
        log.info("BookingController: Запрос на получие всех бронирований текущего пользователя с ID={}", userId);
        BookingStatus status = BookingStatus.from(state).orElseThrow(() -> new IllegalArgumentException("Такой статус у броней не предусмотрен:" + state));
        return bookingClient.getBookingByUser(userId, status.name());
    }
}
