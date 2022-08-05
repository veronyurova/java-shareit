package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestServiceIntegrationTest {
    private final EntityManager manager;
    private final ItemRequestService itemRequestService;
    private final User user1 = new User(1L, "User 1", "user1@mail.com");
    private final User user2 = new User(2L, "User 2", "user2@mail.com");
    private final User userAdd1 = new User(null, "User 1", "user1@mail.com");
    private final User userAdd2 = new User(null, "User 2", "user2@mail.com");
    private final ItemRequest itemRequestAdd1 = new ItemRequest(null, "Request 1", user1,
            LocalDateTime.MIN);
    private final ItemRequest itemRequestAdd2 = new ItemRequest(null, "Request 2", user1,
            LocalDateTime.MIN);
    private final ItemRequest itemRequestAdd3 = new ItemRequest(null, "Request 3", user2,
            LocalDateTime.MIN);

    @Test
    void getAllRequests() {
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemRequestAdd1);
        manager.persist(itemRequestAdd2);
        manager.persist(itemRequestAdd3);

        List<ItemRequestDto> requests = itemRequestService.getAllRequests(2L, 0 ,10);

        assertNotNull(requests);
        assertEquals(2, requests.size());
        assertEquals(1L, requests.get(0).getId());
        assertEquals("Request 1", requests.get(0).getDescription());
        assertEquals(2L, requests.get(1).getId());
        assertEquals("Request 2", requests.get(1).getDescription());
    }

    @Test
    void getUserRequests() {
        manager.persist(userAdd1);
        manager.persist(userAdd2);
        manager.persist(itemRequestAdd1);
        manager.persist(itemRequestAdd2);
        manager.persist(itemRequestAdd3);

        List<ItemRequestDto> requests = itemRequestService.getUserRequests(1L);

        assertNotNull(requests);
        assertEquals(2, requests.size());
        assertEquals(1L, requests.get(0).getId());
        assertEquals("Request 1", requests.get(0).getDescription());
        assertEquals(2L, requests.get(1).getId());
        assertEquals("Request 2", requests.get(1).getDescription());
    }

    @Test
    void getRequestById() {
        manager.persist(userAdd1);
        manager.persist(itemRequestAdd1);

        ItemRequestDto request = itemRequestService.getRequestById(1L, 1L);

        assertNotNull(request);
        assertEquals(1L, request.getId());
        assertEquals("Request 1", request.getDescription());
    }

    @Test
    void addRequest() {
        ItemRequestDto itemRequestDtoAdd = new ItemRequestDto(null, "Request", null, null, null);
        manager.persist(userAdd1);

        itemRequestService.addRequest(1L, itemRequestDtoAdd);

        String queryString = "SELECT r FROM ItemRequest r WHERE r.id = 1";
        TypedQuery<ItemRequest> query = manager.createQuery(queryString, ItemRequest.class);
        ItemRequest request = query.getSingleResult();

        assertNotNull(request);
        assertEquals(1L, request.getId());
        assertEquals(itemRequestDtoAdd.getDescription(), request.getDescription());
    }
}
