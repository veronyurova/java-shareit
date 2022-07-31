package ru.practicum.shareit.request;

import javax.validation.Valid;
import java.util.List;

public interface ItemRequestService {
    List<ItemRequestDto> getAllRequests(int from, int size);

    List<ItemRequestDto> getUserRequests(Long userId);

    ItemRequestDto getRequestById(Long userId, Long requestId);

    ItemRequestDto addRequest(Long userId, @Valid ItemRequestDto itemRequestDto);
}
