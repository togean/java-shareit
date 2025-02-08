package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ItemRepository itemRepository;

    @Override
    public List<UserDto> getAllUsers() {
        log.info("UserServiceImpl: Запрос на получение всех пользователей");
        return userMapper.toListOfUserDto(userRepository.findAll());
    }

    @Override
    public UserDto getUserById(Integer userId) {
        log.info("UserServiceImpl: Запрос на получение пользователя с ID={}", userId);
        Optional<User> user = userRepository.findById(userId);
        return user.map(userMapper::toUserDto).orElse(null);
    }

    @Override
    public UserDto addNewUser(UserDto user) {
        log.info("UserServiceImpl: Запрос на добавление пользователя {}", user);
        if (isValid(userMapper.toUser(user))) {
            User newUser = userRepository.save(userMapper.toUser(user));
            return userMapper.toUserDto(newUser);
        }
        return null;
    }

    @Override
    public UserDto changeUser(Integer userId, UserDto user) {
        log.info("UserServiceImpl: Запрос на обновление данных пользователя {}", user);
        //Сначала проверим, что нет другого пользователя с таким же email
        List<UserDto> listOfUsers = getAllUsers();
        for (UserDto userToCheck : listOfUsers) {
            if (userToCheck.getEmail().equals(user.getEmail())) {
                if (!userToCheck.getName().equals(user.getName())) {
                    throw new ValidationException("Существует другой пользователь стаким email");
                }
            }
        }
        Optional<User> newUser = userRepository.findById(userId);
        if (newUser.isPresent()) {
            if (user.getEmail() != null) {
                newUser.get().setEmail(user.getEmail());
            }
            if (user.getName() != null) {
                newUser.get().setName(user.getName());
            }
            if (isValid(newUser.get())) {
                return userMapper.toUserDto(userRepository.save(newUser.get()));
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        log.info("UserServiceImpl: Запрос на удаление пользователя с ID={}", userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            itemRepository.deleteByOwner_id(userId);
            userRepository.deleteById(userId);
        }
    }

    private boolean isValid(User user) {
        boolean result = true;
        if (user.getName().isEmpty()) {
            throw new ValidationException("Имя пользователя не может быть пустым");
        }
        if (user.getEmail().isEmpty()) {
            throw new ValidationException("Email пользователя не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Неверный формат почтового адреса");
        }
        return result;
    }
}
