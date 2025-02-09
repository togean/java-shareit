package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoWithItem;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    @SneakyThrows
    void addBooking() {
        BookingStatus status = BookingStatus.WAITING;
        BookingDto booking = new BookingDto();
        BookingDtoWithItem responseBooking = new BookingDtoWithItem();
        Item item = new Item();
        User user = new User();
        item.setName("Шуруповёрт");
        item.setDescription("Шуруповёрт");
        item.setId(1);

        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        booking.setBooker(user);
        booking.setId(1);
        booking.setStatus(status);

        responseBooking.setItem(item);
        responseBooking.setBooker(user);
        responseBooking.setId(1);
        responseBooking.setStatus(status);

        when(bookingService.addBooking(booking, 1)).thenReturn(responseBooking);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId())
                        .content(new ObjectMapper().writeValueAsString(booking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.item.id").value(1))
                .andExpect(jsonPath("$.item.name").value(item.getName()))
                .andExpect(jsonPath("$.booker.id").value(user.getId()))
                .andExpect(jsonPath("$.status").value("WAITING"));

        verify(bookingService, times(1)).addBooking(booking, user.getId());
    }

    @Test
    @SneakyThrows
    void bookingUpdateStatus() {
        BookingStatus status = BookingStatus.APPROVED;

        BookingDtoWithItem responseBooking = new BookingDtoWithItem();
        Item item = new Item();
        User user = new User();

        item.setName("Шуруповёрт");
        item.setDescription("Шуруповёрт");
        item.setId(1);

        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        responseBooking.setItem(item);
        responseBooking.setBooker(user);
        responseBooking.setId(1);
        responseBooking.setStatus(status);

        Boolean approvement = true;
        when(bookingService.bookingUpdateStatus(1, 1, approvement)).thenReturn(responseBooking);

        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .param("approved", approvement.toString())
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseBooking.getId()))
                .andExpect(jsonPath("$.status").value("APPROVED"));

        verify(bookingService, times(1)).bookingUpdateStatus(1, 1, approvement);
    }

    @Test
    @SneakyThrows
    void getBookingById() {
        BookingStatus status = BookingStatus.WAITING;

        BookingDtoWithItem booking = new BookingDtoWithItem();
        Item item = new Item();
        User user = new User();

        item.setName("Шуруповёрт");
        item.setDescription("Шуруповёрт");
        item.setId(1);

        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        booking.setItem(item);
        booking.setBooker(user);
        booking.setId(1);
        booking.setStatus(status);

        when(bookingService.getBookingById(user.getId(), booking.getId())).thenReturn(booking);

        mockMvc.perform(get("/bookings/{bookingId}", booking.getId())
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.item.id").value(1))
                .andExpect(jsonPath("$.booker.id").value(user.getId()))
                .andExpect(jsonPath("$.status").value("WAITING"));

        verify(bookingService, times(1)).getBookingById(user.getId(), booking.getId());
    }

    @Test
    @SneakyThrows
    void getBookingByOwner() {
        BookingStatus firstBookingStatus = BookingStatus.WAITING;
        BookingStatus secondBookingStatus = BookingStatus.APPROVED;

        BookingDtoWithItem booking = new BookingDtoWithItem();
        BookingDtoWithItem booking2 = new BookingDtoWithItem();
        Item item = new Item();
        User user = new User();

        item.setName("Шуруповёрт");
        item.setDescription("Шуруповёрт");
        item.setId(1);

        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        booking.setItem(item);
        booking.setBooker(user);
        booking.setId(1);
        booking.setStatus(firstBookingStatus);

        booking2.setItem(item);
        booking2.setBooker(user);
        booking2.setId(2);
        booking2.setStatus(secondBookingStatus);

        when(bookingService.getBookingByOwner(user.getId(), "ALL")).thenReturn(List.of(booking, booking2));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", user.getId())
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].booker.name").value(user.getName()))
                .andExpect(jsonPath("$[1].item.name").value(item.getName()));

        verify(bookingService, times(1)).getBookingByOwner(user.getId(), "ALL");
    }

}
