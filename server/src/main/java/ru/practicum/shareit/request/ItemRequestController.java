package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getUserRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long requestId) {
        return itemRequestService.getRequestById(userId, requestId);
    }

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addRequest(userId, itemRequestDto);
    }
}
