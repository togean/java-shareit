package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoWithItem;
import ru.practicum.shareit.exception.NotFoundException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoWithItem addBooking(@RequestHeader("X-Sharer-User-Id") Integer bookingId, @RequestBody BookingDto bookingDto) {
        log.info("BookingController: Запрос на добавление бронирования");
        return bookingService.addBooking(bookingDto, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoWithItem bookingUpdateStatus(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer bookingId, @RequestParam Boolean approved) {
        log.info("BookingController: Запрос на обновление статуса у бронирования с ID={}", bookingId);
        return bookingService.bookingUpdateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoWithItem getBookingById(@RequestHeader("X-Sharer-User-Id") Integer bookerId, @PathVariable Integer bookingId) {
        log.info("BookingController: Запрос на получение данных о бронировании с ID={}", bookingId);
        return bookingService.getBookingById(bookerId, bookingId);
    }

    @GetMapping("/owner")
    public List<BookingDtoWithItem> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") Integer ownerId, @RequestParam(defaultValue = "ALL") String state) {
        log.info("BookingController: Запрос на получие всех бронирований для вещей пользователя с ID={}", ownerId);
        return bookingService.getBookingByOwner(ownerId, state);
    }

    @GetMapping
    public List<BookingDtoWithItem> getBookingByUser(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam(defaultValue = "ALL") String state) {
        log.info("BookingController: Запрос на получие всех бронирований текущего пользователя с ID={}", userId);
        return bookingService.getBookingByUser(userId, state);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        return Map.of("Ошибка", e.getMessage());
    }
}
