package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void initialization() {

        userRepository.deleteAll();
    }

    @Test
    void ifChangeUser_ThenUserWasChanged() {
        User user = new User();
        user.setId(1);
        user.setName("user1");
        user.setEmail("user1@email");
        User savedUser = userRepository.save(user);

        UserDto userUpdate = new UserDto();
        userUpdate.setId(1);
        userUpdate.setName("user1");
        userUpdate.setEmail("user-1@email");



        UserDto resultUser = userService.changeUser(user.getId(), userUpdate);
        assertNotNull(resultUser);
        assertEquals(userUpdate.getName(), resultUser.getName());
        assertEquals(userUpdate.getEmail(), resultUser.getEmail());

    }

    @Test
    void ifWrongUserId_ThenThrowsNotFoundException() {
        Integer nonExistingId = 1000;
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.getUserById(nonExistingId));
        assertTrue(exception.getMessage().contains(String.valueOf(nonExistingId)));
    }

    @Test
    void ifEmailAlreadyExist_ThenThrowsValidationException() {
        User user = new User();
        user.setId(2);
        user.setName("user1");
        user.setEmail("user1@email");

        UserDto userUpdate = new UserDto();
        userUpdate.setId(3);
        userUpdate.setName("user2");
        userUpdate.setEmail("user1@email");

        User savedUser = userRepository.save(user);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.changeUser(userUpdate.getId(), userUpdate));
        assertTrue(exception.getMessage().contains("Существует другой пользователь стаким email"));
    }

    @Test
    void userCanBeDeleted() {
        User user = new User();
        user.setId(4);
        user.setName("user1");
        user.setEmail("user1@email");
        User savedUser = userRepository.save(user);
        userService.deleteUser(user.getId());
        assertThrows(NotFoundException.class, () -> userService.getUserById(user.getId()));
    }
}