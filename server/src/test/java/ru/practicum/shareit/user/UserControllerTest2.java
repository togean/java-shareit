package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest2 {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @SneakyThrows
    void getUserById() {
        Integer userId = 1;
        UserDto user = new UserDto();
        when(userService.getUserById(userId)).thenReturn(user);
        mockMvc.perform(get("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @SneakyThrows
    void addUser() {
        UserDto newUser = new UserDto();
        when(userService.addNewUser(newUser)).thenReturn(newUser);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(newUser), result);
    }

    @Test
    @SneakyThrows
    void deleteUser() {
        Integer userId = 1;
        doNothing().when(userService).deleteUser(userId);
        mockMvc.perform(delete("/users/{id}", userId)).andExpect(status().isOk());
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @SneakyThrows
    void changeUser() {
        UserDto userToBeEdited = new UserDto();
        userToBeEdited.setId(1);
        userToBeEdited.setName("user1");
        userToBeEdited.setEmail("user1@email");

        when(userService.changeUser(userToBeEdited.getId(), userToBeEdited)).thenReturn(userToBeEdited);

        mockMvc.perform(
                        patch("/users/{id}", userToBeEdited.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(userToBeEdited)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userToBeEdited.getId()));

        verify(userService, times(1)).changeUser(userToBeEdited.getId(), userToBeEdited);
    }
}
