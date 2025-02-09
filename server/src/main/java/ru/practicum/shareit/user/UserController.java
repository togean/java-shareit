package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("UserController: Выполнение запроса на получение всех пользователей");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable(name = "userId") Integer userId) {
        log.info("UserController: Выполнение запроса на получение данных пользователя с ID={}", userId);
        return userService.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserDto user) {
        log.info("UserController: Выполнение запроса на добавление пользователя {}", user);
        return userService.addNewUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto changeUser(@PathVariable(name = "userId") String userId,
                              @RequestBody UserDto user) {
        log.info("UserController: Выполнение запроса на обновление данных пользователя {}", user);
        if (userId != null) {
            Integer userID = Integer.parseInt(userId);
            return userService.changeUser(userID, user);
        }
        return null;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable(name = "userId") Integer userId) {
        log.info("UserController: Выполнение запроса на удаление пользователя с ID={}", userId);
        userService.deleteUser(userId);
    }
}
