package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Integer id;
    private String text;
    private Item item;
    private String authorName;
    private LocalDateTime created;
}
