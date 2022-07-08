package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.InMemoryUserStorage;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.AccessDeniedException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryItemStorage implements ItemStorage {
    private final InMemoryUserStorage userStorage;
    private final Map<Long, Item> items = new HashMap<>();
    private long nextItemId = 0;

    @Autowired
    public InMemoryItemStorage(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<Item> findOwnerItems(Long userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String text) {
        return items.values()
                .stream()
                .filter(
                        item -> item.getAvailable() &&
                        (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                )
                .collect(Collectors.toList());
    }

    @Override
    public Item findItemById(Long id) {
        if (!items.containsKey(id)) {
            String message = String.format("There is no item with id %d", id);
            log.warn("ItemNotFoundException at InMemoryItemStorage.findUserById: {}", message);
            throw new ItemNotFoundException(message);
        }
        return items.get(id);
    }

    @Override
    public Item addItem(Long userId, Item item) {
        item.setOwner(userStorage.findUserById(userId));
        item.setId(getItemId());
        items.put(item.getId(), item);
        log.info("InMemoryItemStorage.addItem: item {} " +
                 "successfully added to storage", item.getId());
        return item;
    }

    @Override
    public Item updateItem(Long userId, Long id, Item newItem) {
        Item item = findItemById(id);
        if (!userId.equals(item.getOwner().getId())) {
            String message = String.format("User %d is not allowed to change item %d", userId, id);
            log.warn("AccessDeniedException at InMemoryItemStorage.updateItem: {}", message);
            throw new AccessDeniedException(message);
        }
        if (newItem.getName() != null && !newItem.getName().isBlank()) {
            item.setName(newItem.getName());
        }
        if (newItem.getDescription() != null && !newItem.getDescription().isBlank()) {
            item.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) item.setAvailable(newItem.getAvailable());
        log.info("InMemoryItemStorage.updateItem: item {} " +
                 "successfully updated", item.getId());
        return item;
    }

    @Override
    public void deleteItemById(Long userId, Long id) {
        Item item = findItemById(id);
        if (!userId.equals(item.getOwner().getId())) {
            String message = String.format("User %d is not allowed to delete item %d", userId, id);
            log.warn("AccessDeniedException at InMemoryItemStorage.deleteItemById: {}", message);
            throw new AccessDeniedException(message);
        }
        items.remove(id);
        log.info("InMemoryItemStorage.deleteItemById: item {} " +
                 "successfully deleted from storage", id);
    }

    @Override
    public void deleteAllItems() {
        items.clear();
        nextItemId = 0;
    }

    private Long getItemId() {
        nextItemId += 1;
        return nextItemId;
    }
}
