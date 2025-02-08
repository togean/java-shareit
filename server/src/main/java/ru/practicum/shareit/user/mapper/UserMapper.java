package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        return userDto;
    }

    public User toUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setId(userDto.getId());
        return user;
    }

    public List<UserDto> toListOfUserDto(List<User> users) {
        List<UserDto> dtoUsers = new ArrayList<>();
        for (User user : users) {
            UserDto newUser = toUserDto(user);
            dtoUsers.add(newUser);
        }
        return dtoUsers;
    }
}
