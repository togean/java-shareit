package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
public class RequestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    @SneakyThrows
    void addItemRequest() {
        Integer userId = 1;
        ItemRequestDto itemRequest = new ItemRequestDto();
        itemRequest.setId(1);
        itemRequest.setDescription("Нужен шуруповёрт");
        itemRequest.setRequester(1);

        when(itemRequestService.addItemRequest(userId, itemRequest)).thenReturn(itemRequest);
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId)
                        .content(new ObjectMapper().writeValueAsString(itemRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.description").value("Нужен шуруповёрт"));

        verify(itemRequestService, times(1)).addItemRequest(userId, itemRequest);
    }

    @Test
    @SneakyThrows
    void getAllItemRequestsByRequester() {
        Integer userId = 1;
        List<ItemRequestDtoWithItem> listOfItemRequests = new ArrayList<>();

        ItemRequestDtoWithItem itemRequest = new ItemRequestDtoWithItem();
        itemRequest.setId(1);
        itemRequest.setDescription("Нужен шуруповёрт");
        itemRequest.setRequester(1);

        listOfItemRequests.add(itemRequest);

        when(itemRequestService.getAllItemRequestsByRequester(userId)).thenReturn(listOfItemRequests);
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].description").value("Нужен шуруповёрт"));

        verify(itemRequestService, times(1)).getAllItemRequestsByRequester(userId);
    }

    @Test
    @SneakyThrows
    void getAllItemRequests() {
        Integer userId = 1;
        List<ItemRequestDto> listOfItemRequests = new ArrayList<>();

        ItemRequestDto itemRequest = new ItemRequestDto();
        itemRequest.setId(1);
        itemRequest.setDescription("Нужен шуруповёрт");
        itemRequest.setRequester(1);

        listOfItemRequests.add(itemRequest);

        when(itemRequestService.getAllItemRequests(userId)).thenReturn(listOfItemRequests);
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].description").value("Нужен шуруповёрт"));

        verify(itemRequestService, times(1)).getAllItemRequests(userId);
    }


}
