package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("UserController: Выполнение запроса на получение всех пользователей");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable(name = "userId") Integer userId) {
        log.info("UserController: Выполнение запроса на получение данных пользователя с ID={}", userId);
        return userClient.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addUser(@RequestBody UserDto user) {
        log.info("UserController: Выполнение запроса на добавление пользователя {}", user);
        return userClient.addNewUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> changeUser(@PathVariable(name = "userId") String userId,
                                             @RequestBody UserDto user) {
        log.info("UserController: Выполнение запроса на обновление данных пользователя {}", user);
        if (userId != null) {
            Integer userID = Integer.parseInt(userId);
            return userClient.changeUser(userID, user);
        }
        return null;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "userId") Integer userId) {
        log.info("UserController: Выполнение запроса на удаление пользователя с ID={}", userId);
        return userClient.deleteUser(userId);
    }
}
