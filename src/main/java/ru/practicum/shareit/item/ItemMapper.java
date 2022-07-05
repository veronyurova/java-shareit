package ru.practicum.shareit.item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                new ItemDto.User(item.getOwner().getId(), item.getOwner().getName()),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }
}
