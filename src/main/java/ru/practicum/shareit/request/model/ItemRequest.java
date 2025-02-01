package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="requests")
@Data
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="description", nullable = false, length = 255)
    private String description;
    @Column(name="requester_id", nullable = false)
    private Integer requestor;
    @Column(name="created_time", nullable = false)
    private LocalDateTime created;
}

