package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;


import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

@Slf4j
@Service
@Validated
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserService userService,
                           BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<Item> getOwnerItems(Long userId) {
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text.isBlank()) return Collections.emptyList();
        return itemRepository.searchItems(text);
    }

    @Override
    public Item getItemById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            String message = String.format("There is no item with id %d", id);
            log.warn("EntityNotFoundException at ItemServiceImpl.getItemById: {}", message);
            throw new EntityNotFoundException(message);
        }
        return item.get();
    }

    @Override
    public Item addItem(Long userId, @Valid Item item) {
        item.setOwner(userService.getUserById(userId));
        Item addedItem = itemRepository.save(item);
        log.info("ItemServiceImpl.addItem: item {} successfully added", item.getId());
        return addedItem;
    }

    @Override
    public Item updateItem(Long userId, Long id, Item newItem) {
        Item item = getItemById(id);
        if (!userId.equals(item.getOwner().getId())) {
            String message = String.format("User %d is not allowed to change item %d", userId, id);
            log.warn("AccessDeniedException at ItemServiceImpl.updateItem: {}", message);
            throw new AccessDeniedException(message);
        }
        if (newItem.getName() != null && !newItem.getName().isBlank()) {
            item.setName(newItem.getName());
        }
        if (newItem.getDescription() != null && !newItem.getDescription().isBlank()) {
            item.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) item.setAvailable(newItem.getAvailable());
        Item updatedItem = itemRepository.save(item);
        log.info("ItemServiceImpl.updateItem: item {} successfully updated", item.getId());
        return updatedItem;
    }

    @Override
    public void deleteItemById(Long userId, Long id) {
        Item item = getItemById(id);
        if (!userId.equals(item.getOwner().getId())) {
            String message = String.format("User %d is not allowed to delete item %d", userId, id);
            log.warn("AccessDeniedException at ItemServiceImpl.deleteItemById: {}", message);
            throw new AccessDeniedException(message);
        }
        itemRepository.deleteById(id);
        log.info("ItemServiceImpl.deleteItemById: item {} successfully deleted", id);
    }

    protected void deleteAllItems() {
        itemRepository.deleteAll();
    }

    @Override
    public void addLastAndNextBooking(ItemDto itemDto) {
        itemDto.setLastBooking(
                bookingRepository.findFirstByItemIdAndEndIsBeforeOrderByEndDesc(
                        itemDto.getId(),
                        LocalDateTime.now())
        );
        itemDto.setNextBooking(bookingRepository.findFirstByItemIdAndStartIsAfterOrderByStartAsc(
                itemDto.getId(),
                LocalDateTime.now())
        );
    }
}
