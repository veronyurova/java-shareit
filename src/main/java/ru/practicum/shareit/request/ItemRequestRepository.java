package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("SELECT r FROM ItemRequest AS r " +
            "ORDER BY r.created DESC")
    List<ItemRequest> findAll();

    @Query("SELECT r FROM ItemRequest AS r " +
           "WHERE r.requester.id = ?1 " +
           "ORDER BY r.created DESC")
    List<ItemRequest> findByRequesterId(Long requesterId);
}
