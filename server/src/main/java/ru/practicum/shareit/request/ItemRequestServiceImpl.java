package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoShortForRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserServiceImpl userService;
    private final UserMapper userMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemRequestDto addItemRequest(Integer requesterId, ItemRequestDto requestToBeAdded) {
        log.info("ItemRequestServiceImpl: Выполнение запроса на создание нового реквеста на вещь");
        User requester = userMapper.toUser(userService.getUserById(requesterId));
        ItemRequest newItemRequest = itemRequestMapper.toItemRequest(requestToBeAdded, LocalDateTime.now(), requester);
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(newItemRequest));
    }

    @Override
    public List<ItemRequestDtoWithItem> getAllItemRequestsByRequester(Integer requesterId) {
        log.info("ItemRequestServiceImpl: Выполнение запроса на вывод всех реквестов пользователя");
        if (userService.getUserById(requesterId) != null) {
            List<ItemRequest> listOfItemRequests = itemRequestRepository.findAllByRequester_Id(requesterId);
            List<ItemRequestDtoWithItem> resultList = new ArrayList<>();
            for (ItemRequest itemRequest : listOfItemRequests) {
                List<ItemDtoShortForRequest> listOfItems = itemMapper.toListOfItemDtoShortForRequest(itemRepository.findAllByRequestId(itemRequest.getId()));
                ItemRequestDtoWithItem itemRequestShort = itemRequestMapper.toItemRequestDtoWithItems(itemRequest, listOfItems);
                resultList.add(itemRequestShort);
            }
            log.info("Реквесты пользователя: {}", resultList);
            return resultList;
        }
        throw new NotFoundException("Данный пользователь не найден");
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Integer requesterId) {
        log.info("ItemRequestServiceImpl: Выполнение запроса на вывод всех реквестов");
        UserDto user = userService.getUserById(requesterId);
        if (user != null) {
            return itemRequestMapper.toListOfItemRequestDto(itemRequestRepository.findAllOrderByCreated_Time(user));
        }
        throw new NotFoundException("Пользователь, запрашивающий реквесты, не найден");
    }

    @Override
    public ItemRequestDtoWithItem getItemRequestById(Integer requestId) {
        log.info("ItemRequestServiceImpl: Выполнение запроса на вывод информации по реквесту");
        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(requestId);
        if (itemRequest.isPresent()) {
            log.info("Вот он, реквест по ID={}:{}", requestId, itemRequest);
            List<ItemDtoShortForRequest> listOfItems = itemMapper.toListOfItemDtoShortForRequest(itemRepository.findAllByRequestId(itemRequest.get().getId()));

            if (listOfItems != null) {
                return itemRequestMapper.toItemRequestDtoWithItems(itemRequest.get(), listOfItems);
            }
        }
        throw new NotFoundException("Запрашиваемый реквест не найден");
    }
}
