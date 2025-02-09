package ru.practicum.shareit;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@Sql(scripts = {"file:src/main/resources/schema.sql"})
public class RequestServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void initialization() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void canAddNewItemRequest() {
        User user = new User();
        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        User createdUser = userRepository.save(user);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1);
        itemRequestDto.setRequester(user.getId());
        itemRequestDto.setDescription("Запрос на шуруповёрт");

        ItemRequestDto result = itemRequestService.addItemRequest(1, itemRequestDto);

        assertNotNull(result);
        assertEquals(itemRequestDto.getDescription(), result.getDescription());
    }

    @Test
    void canGetAllRequestByUser() {
        User user = new User();
        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        User createdUser = userRepository.save(user);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1);
        itemRequestDto.setRequester(user.getId());
        itemRequestDto.setDescription("Запрос на шуруповёрт");

        ItemRequestDto result = itemRequestService.addItemRequest(1, itemRequestDto);

        ItemRequestDto itemRequestDto2 = new ItemRequestDto();
        itemRequestDto2.setId(2);
        itemRequestDto2.setRequester(user.getId());
        itemRequestDto2.setDescription("Запрос на дрель");

        ItemRequestDto result2 = itemRequestService.addItemRequest(1, itemRequestDto2);

        List<ItemRequestDtoWithItem> listOfRequests = itemRequestService.getAllItemRequestsByRequester(user.getId());
        assertNotNull(listOfRequests);
        assertEquals(2, listOfRequests.size());
        assertEquals(itemRequestDto.getDescription(), listOfRequests.get(0).getDescription());
        assertEquals(itemRequestDto2.getDescription(), listOfRequests.get(1).getDescription());

    }

    @Test
    void ifTryToGetRequestWithWrongId_thenThrowsNotFoundException() {
        Integer itemRequestId = 10;
        NotFoundException exception = assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequestById(itemRequestId));
        assertTrue(exception.getMessage().contains("Запрашиваемый реквест не найден"));
    }

    @Test
    void ifTryToGetRequestWithWrongUser_thenThrowsNotFoundException() {
        Integer userId = 10;
        assertThrows(NotFoundException.class, () -> itemRequestService.getAllItemRequestsByRequester(userId));
    }
}
