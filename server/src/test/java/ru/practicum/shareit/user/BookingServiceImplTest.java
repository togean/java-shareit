package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoWithItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class BookingServiceImplTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    void initialization() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    void canAddNewBooking() {
        User user = new User();
        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        User createdUser = userRepository.save(user);

        Item item = new Item();
        item.setId(1);
        item.setName("Шуруповёрт");
        item.setDescription("Шуруповёрт");
        item.setAvailable(true);
        item.setOwner(user);

        Item createdItem1 = itemRepository.save(item);

        Booking bookingToAdd = new Booking();
        bookingToAdd.setItem(item);
        bookingToAdd.setId(1);
        bookingToAdd.setBooker(user);
        bookingToAdd.setEnd(LocalDateTime.now().plusDays(5));
        bookingToAdd.setStart(LocalDateTime.now());
        bookingToAdd.setStatus(BookingStatus.WAITING);

        Booking createdBooking = bookingRepository.save(bookingToAdd);
        assertNotNull(createdBooking);
        assertEquals(bookingToAdd.getBooker(), createdBooking.getBooker());
        assertEquals(bookingToAdd.getItem(), createdBooking.getItem());
        assertEquals(bookingToAdd.getStart(), createdBooking.getStart());
        assertEquals(bookingToAdd.getEnd(), createdBooking.getEnd());
    }

    @Test
    void canUpdateBookingStatusFrom_WAITING_to_APPROVED() {
        User user = new User();
        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        User createdUser = userRepository.save(user);

        Item item = new Item();
        item.setId(1);
        item.setName("Шуруповёрт");
        item.setDescription("Шуруповёрт");
        item.setAvailable(true);
        item.setOwner(user);

        Item createdItem1 = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setId(1);
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.now().plusDays(5));
        booking.setStart(LocalDateTime.now());
        booking.setStatus(BookingStatus.WAITING);

        Booking createdBooking = bookingRepository.save(booking);

        BookingDtoWithItem result = bookingService.bookingUpdateStatus(createdBooking.getId(), user.getId(), true);
        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    void canGetBookingById() {
        User user = new User();
        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        User createdUser = userRepository.save(user);

        Item item = new Item();
        item.setId(1);
        item.setName("Шуруповёрт");
        item.setDescription("Шуруповёрт");
        item.setAvailable(true);
        item.setOwner(user);

        Item createdItem1 = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setId(1);
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.now().plusDays(5));
        booking.setStart(LocalDateTime.now());
        booking.setStatus(BookingStatus.WAITING);

        Booking createdBooking = bookingRepository.save(booking);

        BookingDtoWithItem result = bookingService.getBookingById(user.getId(), booking.getId());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getItem(), result.getItem());
        assertEquals(booking.getBooker(), result.getBooker());
    }

    @Test
    void tryToCreateBookingWhenItemIsNotAvailable_thenThrowValidationException() {
        User user = new User();
        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        User createdUser = userRepository.save(user);

        Item item = new Item();
        item.setId(1);
        item.setName("Шуруповёрт");
        item.setDescription("Шуруповёрт");
        item.setAvailable(false);
        item.setOwner(user);

        Item createdItem1 = itemRepository.save(item);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1);
        bookingDto.setBooker(user);
        bookingDto.setEnd(LocalDateTime.now().plusDays(5));
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setItemId(item.getId());

        ValidationException exception = assertThrows(ValidationException.class, () -> bookingService.addBooking(bookingDto, user.getId()));
        assertTrue(exception.getMessage().contains("Выбранный предмет пока недоступен к бронированию"));
    }

    @Test
    void ifTryToCreateBookingInThePast_thenThrowsValidationException() {
        User user = new User();
        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        User createdUser = userRepository.save(user);

        Item item = new Item();
        item.setId(1);
        item.setName("Шуруповёрт");
        item.setDescription("Шуруповёрт");
        item.setAvailable(true);
        item.setOwner(user);

        Item createdItem1 = itemRepository.save(item);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1);
        bookingDto.setBooker(user);
        bookingDto.setEnd(LocalDateTime.now().plusDays(5));
        bookingDto.setStart(LocalDateTime.now().minusDays(1));
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setItemId(item.getId());

        ValidationException exception = assertThrows(ValidationException.class, () -> bookingService.addBooking(bookingDto, user.getId()));
        assertTrue(exception.getMessage().contains("Начало бронирования не может быть в прошлом"));
    }

    @Test
    void ifTryToCreateBookingWithoutDates_thenThrowsValidationException() {
        User user = new User();
        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        User createdUser = userRepository.save(user);

        Item item = new Item();
        item.setId(1);
        item.setName("Шуруповёрт");
        item.setDescription("Шуруповёрт");
        item.setAvailable(true);
        item.setOwner(user);

        Item createdItem1 = itemRepository.save(item);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1);
        bookingDto.setBooker(user);
        bookingDto.setEnd(null);
        bookingDto.setStart(null);
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setItemId(item.getId());

        ValidationException exception = assertThrows(ValidationException.class, () -> bookingService.addBooking(bookingDto, user.getId()));
        assertTrue(exception.getMessage().contains("Начало и конец бронирования должны быть указаны"));
    }

    @Test
    void ifTryToGetBookingInThePast_thenOnlyPastBookingShouldBeShown() {
        User user = new User();
        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        User createdUser = userRepository.save(user);

        Item item = new Item();
        item.setId(1);
        item.setName("Шуруповёрт");
        item.setDescription("Шуруповёрт");
        item.setAvailable(true);
        item.setOwner(user);

        Item createdItem1 = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setBooker(user);
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);

        bookingRepository.save(booking);

        List<BookingDtoWithItem> listOfBooking = bookingService.getBookingByUser(user.getId(), "PAST");
        assertNotNull(listOfBooking);
        assertEquals(1, listOfBooking.size());
        BookingDtoWithItem bookingToCheck = listOfBooking.getFirst();
        assertTrue(bookingToCheck.getEnd().isBefore(LocalDateTime.now()));
    }
}
