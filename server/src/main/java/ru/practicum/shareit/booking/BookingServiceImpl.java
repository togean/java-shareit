package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoWithItem;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDtoWithItem addBooking(BookingDto booking, Integer bookerId) {
        log.info("BookingServiceImpl: Запрос на добавление бронирования");
        if (isValid(booking, bookerId)) {
            Optional<User> user = userRepository.findById(bookerId);
            if (user.isPresent()) {
                booking.setBooker(user.get());
            } else {
                throw new NotFoundException("Пользователь с ID=" + bookerId + " не найден");
            }
            Optional<Item> item = itemRepository.findById(booking.getItemId());
            if (item.isEmpty()) {
                throw new NotFoundException("Предмета указанного в бронировании нет");
            }
            if (!item.get().getAvailable()) {
                throw new ValidationException("Выбранный предмет пока недоступен к бронированию");
            }
            Booking newBooking = bookingMapper.toBooking(booking, item.get());
            newBooking.setBooker(user.get());
            newBooking.setItem(item.get());
            newBooking.setStatus(BookingStatus.WAITING);
            return bookingMapper.toBookingDtoWithItem(bookingRepository.save(newBooking), item.get());
        }
        return null;
    }

    @Override
    public BookingDtoWithItem getBookingById(Integer bookerId, Integer bookingId) {
        log.info("BookingServiceImpl: Запрос информации о бронировании c ID={}", bookingId);
        Optional<User> userWhoRequestBooking = userRepository.findById(bookerId);
        if (userWhoRequestBooking.isPresent()) {
            Optional<Booking> booking = bookingRepository.findById(bookingId);
            if (booking.isPresent()) {
                if ((booking.get().getBooker().getId().equals(bookerId)) || (booking.get().getItem().getOwner().getId().equals(bookerId))) {
                    return bookingMapper.toBookingDtoWithItem(booking.get(), booking.get().getItem());
                } else {
                    throw new NotFoundException("Только владелец бронирования или владелец вещи может просмотреть бронирование");
                }
            }
        }
        throw new NotFoundException("Пользователь, который запрашивает данные по бронированию не найден");
    }

    @Override
    public List<BookingDtoWithItem> getBookingByUser(Integer userId, String state) {
        log.info("BookingServiceImpl: Запрос на получие всех бронирований текущего пользователя с ID={}", userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            LocalDateTime currentTime = LocalDateTime.now();
            if (state.equals("CURRENT")) {
                return bookingMapper.listOfBookingDtoWithItem(bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStart(userId, currentTime, currentTime));
            }
            if (state.equals("PAST")) {
                return bookingMapper.listOfBookingDtoWithItem(bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStart(userId, currentTime));
            }
            if (state.equals("FUTURE")) {
                return bookingMapper.listOfBookingDtoWithItem(bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStart(userId, currentTime));
            }
            if (state.equals("WAITING")) {
                return bookingMapper.listOfBookingDtoWithItem(bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, BookingStatus.WAITING));
            }
            if (state.equals("REJECTED")) {
                return bookingMapper.listOfBookingDtoWithItem(bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, BookingStatus.REJECTED));
            }
            if (state.equals("CANCELED")) {
                return bookingMapper.listOfBookingDtoWithItem(bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, BookingStatus.CANCELED));
            }
            if (state.equals("ALL")) {
                return bookingMapper.listOfBookingDtoWithItem(bookingRepository.findAllByBookerIdOrderByStart(userId));
            }
        }
        throw new NotFoundException("Пользователь с ID=" + userId + " не найден");
    }

    @Override
    public List<BookingDtoWithItem> getBookingByOwner(Integer ownerId, String state) {
        log.info("BookingServiceImpl: Запрос на получие всех бронирований для вещей пользователя с ID={}", ownerId);
        Optional<User> user = userRepository.findById(ownerId);
        if (user.isPresent()) {
            LocalDateTime currentTime = LocalDateTime.now();
            if (state.equals("CURRENT")) {
                return bookingMapper.listOfBookingDtoWithItem(bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStart(ownerId, currentTime, currentTime));
            }
            if (state.equals("PAST")) {
                return bookingMapper.listOfBookingDtoWithItem(bookingRepository.findAllByItemOwnerIdAndEndIsBeforeOrderByStart(ownerId, currentTime));
            }
            if (state.equals("FUTURE")) {
                return bookingMapper.listOfBookingDtoWithItem(bookingRepository.findAllByItemOwnerIdAndStartIsAfterOrderByStart(ownerId, currentTime));
            }
            if (state.equals("WAITING")) {
                return bookingMapper.listOfBookingDtoWithItem(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStart(ownerId, BookingStatus.WAITING));
            }
            if (state.equals("REJECTED")) {
                return bookingMapper.listOfBookingDtoWithItem(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStart(ownerId, BookingStatus.REJECTED));
            }
            if (state.equals("CANCELED")) {
                return bookingMapper.listOfBookingDtoWithItem(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStart(ownerId, BookingStatus.CANCELED));
            }
            if (state.equals("ALL")) {
                return bookingMapper.listOfBookingDtoWithItem(bookingRepository.findAllByItemOwnerIdOrderByStart(ownerId));
            }
        }
        throw new NotFoundException("Пользователь с ID=" + ownerId + " не найден");
    }

    @Override
    public BookingDtoWithItem bookingUpdateStatus(Integer userId, Integer bookingId, Boolean approved) {
        log.info("BookingServiceImpl: Изменение статуса бронирования {} владельцем вещи", bookingId);
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            if (booking.get().getItem().getOwner().getId().equals(userId)) {
                if (approved) {
                    booking.get().setStatus(BookingStatus.APPROVED);
                } else {
                    booking.get().setStatus(BookingStatus.REJECTED);
                }
                return bookingMapper.toBookingDtoWithItem(bookingRepository.save(booking.get()), booking.get().getItem());
            } else {
                throw new ValidationException("Пользователь " + userId + ", пытающийся изменить статус вещи не является её владельцем");
            }
        }
        throw new NotFoundException("Бронирование с ID=" + bookingId + " не найдено");
    }

    boolean isValid(BookingDto booking, Integer bookerId) {
        LocalDateTime startTime = booking.getStart();
        LocalDateTime endTime = booking.getEnd();
        if (startTime == null || endTime == null) {
            throw new ValidationException("Начало и конец бронирования должны быть указаны");
        }
        if (startTime.isAfter(endTime) || endTime.isBefore(startTime)) {
            throw new ValidationException("Начало и конец бронирования указаны не верно");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Начало бронирования не может быть в прошлом");
        }
        if (bookerId == null) {
            throw new ValidationException("Для бронирования не указана вещь, которую пытаются забронировать");
        }
        return true;
    }
}
