package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    @SneakyThrows
    void getItem() {
        Integer userId = 1;
        ItemDtoWithBookingsAndComments item = new ItemDtoWithBookingsAndComments();
        item.setId(1);
        item.setName("Шуруповёрт");
        item.setDescription("Шуруповёрт");
        item.setAvailable(true);
        when(itemService.getItem(userId)).thenReturn(item);
        mockMvc.perform(get("/items/{id}", userId)
                        .header("X-Sharer-User-Id", item.getId()))
                .andDo(print())
                .andExpect(status().isOk());
        verify(itemService, times(1)).getItem(userId);
    }

    @Test
    void getItems() throws Exception {
        Integer userId = 1;
        List<ItemDtoWithBookingsAndComments> items = new ArrayList<>();
        ItemDtoWithBookingsAndComments firstItem = new ItemDtoWithBookingsAndComments();
        firstItem.setName("Шуруповёрт");
        firstItem.setDescription("Шуруповёрт");

        ItemDtoWithBookingsAndComments secondItem = new ItemDtoWithBookingsAndComments();
        secondItem.setName("Дрель");
        secondItem.setDescription("Дрель");

        items.add(firstItem);
        items.add(secondItem);

        when(itemService.getItems(userId)).thenReturn(items);

        mockMvc.perform(get("/items").contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(firstItem.getName()))
                .andExpect(jsonPath("$[1].name").value(secondItem.getName()));

        verify(itemService, times(1)).getItems(userId);
    }

    @Test
    @SneakyThrows
    void searchItem() {
        String searchString = "Др";
        List<ItemDto> items = new ArrayList<>();
        ItemDto firstItem = new ItemDto();
        firstItem.setName("Дрель");
        firstItem.setDescription("Дрель");

        items.add(firstItem);

        when(itemService.searchItem(searchString)).thenReturn(items);
        mockMvc.perform(get("/items/search")
                        .param("text", searchString))
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(firstItem.getName()))
                .andExpect(status().isOk());
        verify(itemService, times(1)).searchItem(searchString);
    }

    @Test
    @SneakyThrows
    void addItem() {
        ItemDto newItem = new ItemDto();
        newItem.setId(1);
        newItem.setName("Дрель");
        newItem.setDescription("Дрель");
        when(itemService.addNewItem(1, newItem)).thenReturn(newItem);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(new ObjectMapper().writeValueAsString(newItem)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value(newItem.getDescription()))
                .andExpect(jsonPath("$.name").value(newItem.getName()));

        verify(itemService, times(1)).addNewItem(1, newItem);
    }

    @Test
    @SneakyThrows
    void changeItem() {
        ItemDto itemToBeEdited = new ItemDto();
        itemToBeEdited.setId(1);
        itemToBeEdited.setName("Дрель");
        itemToBeEdited.setDescription("Дрель");
        when(itemService.changeItem(1, 1, itemToBeEdited)).thenReturn(itemToBeEdited);

        mockMvc.perform(patch("/items/{id}", itemToBeEdited.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(new ObjectMapper().writeValueAsString(itemToBeEdited)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value(itemToBeEdited.getDescription()))
                .andExpect(jsonPath("$.name").value(itemToBeEdited.getName()));

        verify(itemService, times(1)).changeItem(1, 1, itemToBeEdited);
    }

    @Test
    @SneakyThrows
    void deleteItem() {
        Integer itemId = 1;
        Integer userId = 1;
        doNothing().when(itemService).deleteItem(userId, itemId);
        mockMvc.perform(delete("/items/{id}", itemId)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
        verify(itemService, times(1)).deleteItem(userId, itemId);
    }

    @Test
    @SneakyThrows
    void addNewComment() {
        CommentDto newComment = new CommentDto();
        newComment.setText("новый комментарий");
        newComment.setAuthorName("Петя");
        newComment.setId(1);

        when(itemService.addNewComment(1, 1, newComment)).thenReturn(newComment);
        mockMvc.perform(post("/items/{id}/comment", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(new ObjectMapper().writeValueAsString(newComment)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value(newComment.getText()))
                .andExpect(jsonPath("$.authorName").value(newComment.getAuthorName()));
        verify(itemService, times(1)).addNewComment(1, 1, newComment);
    }

}
