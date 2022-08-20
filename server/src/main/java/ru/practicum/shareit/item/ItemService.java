package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    List<ItemDto> getOwnerItems(Long userId, int from, int size);

    List<ItemDto> searchItems(String text, int from, int size);

    ItemDto getItemById(Long userId, Long itemId);

    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    void deleteItemById(Long userId, Long id);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);

    void addLastAndNextBooking(ItemDto itemDto);

    void addCommentsList(ItemDto itemDto);

    List<ItemDto> getItemsByRequestId(Long requestId);
}
