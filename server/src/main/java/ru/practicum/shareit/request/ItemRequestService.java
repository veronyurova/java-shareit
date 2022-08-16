package ru.practicum.shareit.request;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

public interface ItemRequestService {
    List<ItemRequestDto> getAllRequests(Long userId, @Min(0) int from, @Min(1) int size);

    List<ItemRequestDto> getUserRequests(Long userId);

    ItemRequestDto getRequestById(Long userId, Long requestId);

    ItemRequestDto addRequest(Long userId, @Valid ItemRequestDto itemRequestDto);
}
