package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoShortForRequest;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDtoWithItem {
    private Integer id;
    private String description;
    private Integer requester;
    private LocalDateTime created;
    private List<ItemDtoShortForRequest> items;
}
