package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getAllUsers() {
        List<UserDto> expectedUsers = List.of(new UserDto());

        when(userService.getAllUsers()).thenReturn(expectedUsers);
        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUsers, response.getBody());
    }

    @Test
    void getUserById() {
        UserDto expectedUsers = new UserDto();
        expectedUsers.setId(1);
        expectedUsers.setName("user1");
        expectedUsers.setEmail("user1@email");

        when(userService.getUserById(expectedUsers.getId())).thenReturn(expectedUsers);
        UserDto response = userController.getUserById(expectedUsers.getId());

        assertEquals(expectedUsers.getName(), response.getName());
        assertEquals(expectedUsers.getEmail(), response.getEmail());
    }

    @Test
    void addNewUser() {
        UserDto expectedUsers = new UserDto();
        expectedUsers.setId(1);
        expectedUsers.setName("user1");
        expectedUsers.setEmail("user1@email");
        when(userService.addNewUser(expectedUsers)).thenReturn(expectedUsers);
        UserDto response = userService.addNewUser(expectedUsers);

        assertEquals(expectedUsers.getName(), response.getName());
        assertEquals(expectedUsers.getEmail(), response.getEmail());
    }

}
