package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public List<ItemDtoWithBookingsAndComments> getItems(Integer userId) {
        log.info("ItemServiceImpl: Запрос вещей пользователя с ID={}", userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<ItemDtoWithBookingsAndComments> allItemsDtoWithBookings = new ArrayList<>();
            List<Item> items = itemRepository.findAllByOwner_id(userId);
            for (Item item : items) {
                List<BookingDto> bookingsToBeAdded = bookingMapper.listOfBookingDto(bookingRepository.findAllByItem_id(item.getId()));
                int numberOfBookings = bookingsToBeAdded.size();
                if (numberOfBookings > 1) {
                    allItemsDtoWithBookings.add(itemMapper.itemToItemDtoWithBookingsAndComments(item, bookingsToBeAdded.get(numberOfBookings - 1), bookingsToBeAdded.getLast(), commentMapper.toListOfCommentDto(commentRepository.findAllByItem_Id(item.getId()))));
                } else {
                    allItemsDtoWithBookings.add(itemMapper.itemToItemDtoWithoutBookingsOrComments(item, commentMapper.toListOfCommentDto(commentRepository.findAllByItem_Id(item.getId()))));
                }
            }
            return allItemsDtoWithBookings;
        } else {
            throw new NotFoundException("Такого пользователя не существует");
        }
    }

    @Override
    public ItemDtoWithBookingsAndComments getItem(Integer itemId) {
        log.info("ItemServiceImpl: Запрос вещи с ID={}", itemId);
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            List<BookingDto> bookingsToBeAdded = bookingMapper.listOfBookingDto(bookingRepository.findAllByItem_idAndStatusOrderByStartAsc(itemId, BookingStatus.APPROVED));
            List<Comment> listOfComments = commentRepository.findAllByItem(item.get(), Sort.by(Sort.Direction.DESC, "created"));
            int numberOfBookings = bookingsToBeAdded.size();
            if (numberOfBookings > 1) {
                return itemMapper.itemToItemDtoWithBookingsAndComments(item.get(), bookingsToBeAdded.get(numberOfBookings - 1), bookingsToBeAdded.getLast(), commentMapper.toListOfCommentDto(listOfComments));
            } else {
                return itemMapper.itemToItemDtoWithoutBookingsOrComments(item.get(), commentMapper.toListOfCommentDto(listOfComments));
            }
        }
        throw new NotFoundException("Такой вещи не существует");
    }

    @Override
    public ItemDto addNewItem(Integer userId, ItemDto item) {
        log.info("ItemServiceImpl: Запрос на добавление вещи {}", item);

        Item newItem = itemMapper.toItem(item);
        if (isValid(newItem)) {
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                throw new NotFoundException("Такого пользователя нет");
            }
            newItem.setOwner(user.get());
            newItem = itemRepository.save(newItem);
            return itemMapper.toItemDto(newItem);
        }
        return null;
    }

    @Override
    public void deleteItem(Integer userId, Integer itemId) {
        log.info("ItemServiceImpl: Запрос на удаление вещи с ID={}", itemId);
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                if (item.get().getOwner().equals(user.get())) {
                    itemRepository.deleteById(itemId);
                }
            } else {
                throw new NotFoundException("Такого пользователя нет");
            }
        } else {
            throw new NotFoundException("Такой вещи нет");
        }
    }

    @Override
    public ItemDto changeItem(Integer userId, Integer itemId, ItemDto item) {
        log.info("ItemServiceImpl: Запрос на обновление вещи с ID={}", itemId);
        Optional<User> user = userRepository.findById((int) userId);
        if (user.isEmpty()) {
            log.info("Такого пользователя нет");
            throw new NotFoundException("Такого пользователя нет");
        }
        Optional<Item> newItem = itemRepository.findById(itemId);
        if (newItem.isPresent()) {
            if (newItem.get().getOwner().getId().equals(userId)) {
                if (item.getDescription() != null) {
                    newItem.get().setDescription(item.getDescription());
                }
                if (item.getName() != null) {
                    newItem.get().setName(item.getName());
                }
                if (item.getAvailable() != null) {
                    newItem.get().setAvailable(item.getAvailable());
                }
                return itemMapper.toItemDto(itemRepository.save(newItem.get()));
            } else {
                throw new NotFoundException("Пользователь, пытающийся изменить вещь, не является её владельцем");
            }
        }
        return null;
    }

    @Override
    public CommentDto addNewComment(Integer authorId, Integer itemId, CommentDto newComment) {
        log.info("ItemServiceImpl: Запрос на добавление комментария к выбранной вещи");
        Optional<User> user = userRepository.findById(authorId);
        if (user.isPresent()) {
            Optional<Item> item = itemRepository.findById(itemId);
            if (item.isPresent()) {
                LocalDateTime currentTime = LocalDateTime.now();
                List<Booking> endedBookingsList = bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStart(user.get().getId(), currentTime);
                for (Booking booking : endedBookingsList) {
                    if (booking.getItem().getId().equals(itemId)) {
                        newComment.setCreated(currentTime);
                        newComment.setItem(booking.getItem());
                        Comment comment = commentRepository.save(commentMapper.toComment(newComment, user.get()));
                        return commentMapper.toCommentDto(comment, item.get());
                    }
                }
                throw new ValidationException("Пользователь не брал эту вещь в аренду");
            }
            throw new NotFoundException("Вещь, для которой создаётся комментарий не найдена");
        }
        throw new NotFoundException("Пользователь, который создаёт комментарий не найден");
    }

    @Override
    public List<ItemDto> searchItem(String textToSearch) {
        log.info("ItemServiceImpl: Запрос на поиск вещи по тексту \"{}\"", textToSearch);
        if (textToSearch.isEmpty()) {
            return new ArrayList<>();
        }

        List<ItemDto> resultItemsInName = new ArrayList<>();
        List<ItemDto> resultItemsInDescription = new ArrayList<>();

        Optional<List<Item>> lisOfItems = Optional.of(itemRepository.findAll());
        for (Item item : lisOfItems.get()) {
            if (item.getName().toLowerCase().contains(textToSearch.toLowerCase())) {
                if (item.getAvailable()) {
                    resultItemsInName.add(itemMapper.toItemDto(item));
                }
            }
            if (item.getDescription().toLowerCase().contains(textToSearch.toLowerCase())) {
                if (item.getAvailable()) {
                    resultItemsInDescription.add(itemMapper.toItemDto(item));
                }
            }
        }
        Set<ItemDto> setOfItems = new HashSet<>();
        setOfItems.addAll(resultItemsInName);
        setOfItems.addAll(resultItemsInDescription);
        return new ArrayList<>(setOfItems);
    }

    private boolean isValid(Item item) {
        boolean result = true;
        if ((item.getName() == null) || (item.getName().isEmpty())) {
            throw new ValidationException("Название вещи не может быть пустым");
        }
        if ((item.getDescription() == null) || (item.getDescription().isEmpty())) {
            throw new ValidationException("Описание вещи не может быть пустым");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Для вещи должна быть указана доступность");
        }
        return result;
    }
}
