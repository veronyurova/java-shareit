package ru.practicum.shareit.request;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                new ItemRequestDto.User(itemRequest.getRequestor().getId(),
                                        itemRequest.getRequestor().getName()),
                itemRequest.getCreated()
        );
    }
}
