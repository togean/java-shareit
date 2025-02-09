package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ItemDto {
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private Boolean available;
    private Integer requestId;
}
