package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemService itemService;
    private final UserService userService;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                  ItemService itemService, UserService userService) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemService = itemService;
        this.userService = userService;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(int from, int size) {
        return itemRequestRepository.findAll()
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getUserRequests(Long userId) {
        List<ItemRequestDto> requests = itemRequestRepository.findByRequesterId(userId)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        requests.forEach(request -> request.setItems(
                itemService.getItemsByRequestId(request.getId())
                )
        );
        return requests;
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(requestId);
        if (itemRequest.isEmpty()) {
            String message = String.format("There is no request with id %d", requestId);
            log.warn("EntityNotFoundException at ItemRequestServiceImpl.getRequestById: {}",
                     message);
            throw new EntityNotFoundException(message);
        }
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest.get());
        itemRequestDto.setItems(itemService.getItemsByRequestId(requestId));
        return itemRequestDto;
    }

    @Override
    public ItemRequestDto addRequest(Long userId, @Valid ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequester(UserMapper.toUser(userService.getUserById(userId)));
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequest addedItemRequest = itemRequestRepository.save(itemRequest);
        log.info("ItemRequestServiceImpl.addRequest: request {} successfully added",
                 addedItemRequest.getId());
        return ItemRequestMapper.toItemRequestDto(addedItemRequest);
    }
}
