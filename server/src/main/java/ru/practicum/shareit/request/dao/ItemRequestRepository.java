package ru.practicum.shareit.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
    @Query("select i from ItemRequest i where i.requester <> ?1 order by i.created")
    List<ItemRequest> findAllOrderByCreated_Time(UserDto user);

    List<ItemRequest> findAllByRequester_Id(Integer requesterId);

}
