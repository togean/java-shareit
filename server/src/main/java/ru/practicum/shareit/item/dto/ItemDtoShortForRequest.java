package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDtoShortForRequest {
    Integer itemRequestId;
    String name;
    Integer itemOwnerId;
}
