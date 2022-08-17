package ru.practicum.shareit.request;

import java.util.List;

public interface ItemRequestService {
    List<ItemRequestDto> getAllRequests(Long userId, int from, int size);

    List<ItemRequestDto> getUserRequests(Long userId);

    ItemRequestDto getRequestById(Long userId, Long requestId);

    ItemRequestDto addRequest(Long userId, ItemRequestDto itemRequestDto);
}
