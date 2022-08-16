package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Item AS i " +
           "WHERE i.owner.id = ?1 " +
           "ORDER BY i.id ASC")
    List<Item> findByOwnerId(Long id, Pageable pageable);

    @Query("SELECT i FROM Item AS i " +
           "WHERE i.available = true AND " +
           "      (UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) OR " +
           "       UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%')))")
    List<Item> searchItems(String text, Pageable pageable);

    List<Item> findByRequestId(Long requestId);
}
