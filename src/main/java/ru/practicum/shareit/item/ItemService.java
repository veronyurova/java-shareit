package ru.practicum.shareit.item;

import javax.validation.Valid;
import java.util.List;

public interface ItemService {
    List<ItemDto> getOwnerItems(Long userId);

    List<ItemDto> searchItems(String text);

    ItemDto getItemById(Long userId, Long itemId);

    ItemDto addItem(Long userId, @Valid ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    void deleteItemById(Long userId, Long id);

    CommentDto addComment(Long userId, Long itemId, @Valid CommentDto commentDto);

    void addLastAndNextBooking(ItemDto itemDto);

    void addCommentsList(ItemDto itemDto);
}
