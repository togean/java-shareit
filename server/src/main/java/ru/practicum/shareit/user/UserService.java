package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Integer userId);

    UserDto addNewUser(UserDto user);

    UserDto changeUser(Integer userId, UserDto user);

    void deleteUser(Integer userId);
}
