package ru.practicum.shareit.request;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                new ItemRequestDto.UserDto(itemRequest.getRequester().getId(),
                                           itemRequest.getRequester().getName()),
                itemRequest.getCreated(),
                null
        );
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(
                null,
                itemRequestDto.getDescription(),
                null,
                null
        );
    }
}
