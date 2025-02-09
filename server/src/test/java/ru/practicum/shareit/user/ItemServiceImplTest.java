package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class ItemServiceImplTest {
    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void canAddNewCorrectItem() {
        User user = new User();
        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        User createdUser = userRepository.save(user);

        Item newItem = new Item();
        newItem.setId(1);
        newItem.setName("Шуруповёрт");
        newItem.setDescription("Шуруповёрт");
        newItem.setAvailable(true);
        newItem.setOwner(user);

        Item createdItem = itemRepository.save(newItem);
        assertNotNull(createdItem);
        assertEquals(newItem.getName(), createdItem.getName());
        assertEquals(newItem.getDescription(), createdItem.getDescription());
    }

    @Test
    void ifChangeItem_ThenItemWasChanged() {
        User user = new User();
        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        User createdUser = userRepository.save(user);

        Item newItem = new Item();
        newItem.setId(1);
        newItem.setName("Шуруповёрт");
        newItem.setDescription("Шуруповёрт");
        newItem.setAvailable(true);
        newItem.setOwner(user);

        Item createdItem = itemRepository.save(newItem);

        ItemDto itemUpdate = new ItemDto();
        itemUpdate.setId(1);
        itemUpdate.setName("Шуруповёрт DEWALT");
        itemUpdate.setDescription("Отличный шуруповёрт");
        itemUpdate.setAvailable(true);

        ItemDto result = itemService.changeItem(user.getId(), newItem.getId(), itemUpdate);
        assertNotNull(result);
        assertEquals(itemUpdate.getName(), result.getName());
        assertEquals(itemUpdate.getDescription(), result.getDescription());
    }

    @Test
    void itemCanBeDeleted() {
        User user = new User();
        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        User createdUser = userRepository.save(user);

        Item newItem = new Item();
        newItem.setId(1);
        newItem.setName("Шуруповёрт");
        newItem.setDescription("Шуруповёрт");
        newItem.setAvailable(true);
        newItem.setOwner(user);

        Item createdItem = itemRepository.save(newItem);

        itemService.deleteItem(user.getId(), newItem.getId());
        assertThrows(NotFoundException.class, () -> itemService.deleteItem(user.getId(), newItem.getId()));
    }

    @Test
    void canGetCorrectUserItems() {
        User user = new User();
        user.setName("user1");
        user.setEmail("user1@email");
        user.setId(1);

        User createdUser = userRepository.save(user);

        Item newItem1 = new Item();
        newItem1.setId(1);
        newItem1.setName("Шуруповёрт");
        newItem1.setDescription("Шуруповёрт");
        newItem1.setAvailable(true);
        newItem1.setOwner(user);

        Item createdItem1 = itemRepository.save(newItem1);

        Item newItem2 = new Item();
        newItem2.setId(2);
        newItem2.setName("Дрель");
        newItem2.setDescription("Дрель");
        newItem2.setAvailable(true);
        newItem2.setOwner(user);

        Item createdItem2 = itemRepository.save(newItem2);

        List<ItemDtoWithBookingsAndComments> listOfItems = itemService.getItems(user.getId());
        assertNotNull(listOfItems);
        assertEquals(2, listOfItems.size());
        assertTrue(listOfItems.stream().anyMatch(item -> item.getName().equals(createdItem1.getName())));
        assertTrue(listOfItems.stream().anyMatch(item -> item.getName().equals(createdItem1.getName())));

    }
}
