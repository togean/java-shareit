package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByOwner_id(Integer ownerId);

    void deleteByOwner_id(Integer ownerId);
    Item findByRequest_Id(Integer itemRequestId);

    List<Item> findAllByRequestId(Integer requestId);
}
