package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    private ItemRequestService itemRequestService;
    @Mock
    private ItemRequestRepository mockItemRequestRepository;
    @Mock
    private ItemService mockItemService;
    @Mock
    private UserService mockUserService;
    private final User user = new User(1L, "User", "user@mail.com");
    private final UserDto userDto = new UserDto(1L, "User", "user@mail.com");
    private final ItemDto itemDto = new ItemDto(1L, "Item 1", "Test", true,
            new ItemDto.UserDto(2L, "User2"), null, null, null, null);
    private final ItemRequest itemRequestGet = new ItemRequest(1L, "Request", user,
            LocalDateTime.MIN);
    private final ItemRequestDto itemRequestSave = new ItemRequestDto(null, "Request",
            null, null, null);
    private final ItemRequestDto itemRequestExpectedSave = new ItemRequestDto(1L, "Request",
            new ItemRequestDto.UserDto(1L, "User"), LocalDateTime.MIN, null);
    private final ItemRequestDto itemRequestExpected = new ItemRequestDto(1L, "Request",
            new ItemRequestDto.UserDto(1L, "User"), LocalDateTime.MIN, List.of(itemDto));

    @BeforeEach
    void beforeEach() {
        itemRequestService = new ItemRequestServiceImpl(mockItemRequestRepository,
                mockItemService, mockUserService);
    }

    @Test
    void getAllRequests() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(mockItemRequestRepository.findAll(Mockito.anyLong(),
                Mockito.any(Pageable.class))).thenReturn(List.of(itemRequestGet));
        Mockito
                .when(mockItemService.getItemsByRequestId(Mockito.anyLong()))
                .thenReturn(List.of(itemDto));

        List<ItemRequestDto> requests = itemRequestService.getAllRequests(1L, 0, 10);

        assertNotNull(requests);
        assertEquals(itemRequestExpected, requests.get(0));
    }

    @Test
    void getAllRequestsNoRequests() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(mockItemRequestRepository.findAll(Mockito.anyLong(),
                Mockito.any(Pageable.class))).thenReturn(Collections.emptyList());

        List<ItemRequestDto> requests = itemRequestService.getAllRequests(1L, 0, 10);

        assertNotNull(requests);
        assertEquals(0, requests.size());
    }

    @Test
    void getUserRequests() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockItemRequestRepository.findByRequesterId(1L))
                .thenReturn(List.of(itemRequestGet));
        Mockito
                .when(mockItemService.getItemsByRequestId(Mockito.anyLong()))
                .thenReturn(List.of(itemDto));

        List<ItemRequestDto> requests = itemRequestService.getUserRequests(1L);

        assertNotNull(requests);
        assertEquals(itemRequestExpected, requests.get(0));
    }

    @Test
    void getUserRequestsNoRequests() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockItemRequestRepository.findByRequesterId(1L))
                .thenReturn(Collections.emptyList());

        List<ItemRequestDto> requests = itemRequestService.getUserRequests(1L);

        assertNotNull(requests);
        assertEquals(0, requests.size());
    }

    @Test
    void getRequestById() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito
                .when(mockItemRequestRepository.findById(1L))
                .thenReturn(Optional.of(itemRequestGet));
        Mockito.when(mockItemService.getItemsByRequestId(1L)).thenReturn(List.of(itemDto));

        ItemRequestDto request = itemRequestService.getRequestById(1L, 1L);

        assertNotNull(request);
        assertEquals(itemRequestExpected, request);
    }

    @Test
    void getRequestByIdNoSuchRequest() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(mockItemRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> itemRequestService.getRequestById(1L, 1L));
    }

    @Test
    void addComment() {
        Mockito.when(mockUserService.getUserById(Mockito.anyLong())).thenReturn(userDto);
        Mockito
                .when(mockItemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(itemRequestGet);

        ItemRequestDto request = itemRequestService.addRequest(1L, itemRequestSave);

        assertNotNull(request);
        assertEquals(itemRequestExpectedSave, request);
    }
}
