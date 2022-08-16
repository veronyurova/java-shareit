package ru.practicum.shareit.request;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemDto;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private final ItemDto itemDto = new ItemDto(1L, "Item 1", "Test", true,
            new ItemDto.UserDto(1L, "User"), 1L, null, null, null);
    private final ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Request",
            new ItemRequestDto.UserDto(1L, "User"), null, List.of(itemDto));
    private final ItemRequestDto itemRequestDtoAdd = new ItemRequestDto(null, "Request",
            null, null, null);

    @Test
    void getAllRequests() throws Exception {
        when(itemRequestService.getAllRequests(1L, 1, 1)).thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests/all?from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$[0].requester.id")
                        .value(itemRequestDto.getRequester().getId()))
                .andExpect(jsonPath("$[0].requester.name")
                        .value(itemRequestDto.getRequester().getName()))
                .andExpect(jsonPath("$[0].created").value(itemRequestDto.getCreated()))
                .andExpect(jsonPath("$[0].items[0].id")
                        .value(itemRequestDto.getItems().get(0).getId()))
                .andExpect(jsonPath("$[0].items[0].name")
                        .value(itemRequestDto.getItems().get(0).getName()));
    }

    @Test
    void getAllRequestsDefaultPaginationParameters() throws Exception {
        when(itemRequestService.getAllRequests(1L, 0, 10)).thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$[0].requester.id")
                        .value(itemRequestDto.getRequester().getId()))
                .andExpect(jsonPath("$[0].requester.name")
                        .value(itemRequestDto.getRequester().getName()))
                .andExpect(jsonPath("$[0].created").value(itemRequestDto.getCreated()))
                .andExpect(jsonPath("$[0].items[0].id")
                        .value(itemRequestDto.getItems().get(0).getId()))
                .andExpect(jsonPath("$[0].items[0].name")
                        .value(itemRequestDto.getItems().get(0).getName()));
    }

    @Test
    void getUserRequests() throws Exception {
        when(itemRequestService.getUserRequests(1L)).thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$[0].requester.id")
                        .value(itemRequestDto.getRequester().getId()))
                .andExpect(jsonPath("$[0].requester.name")
                        .value(itemRequestDto.getRequester().getName()))
                .andExpect(jsonPath("$[0].created").value(itemRequestDto.getCreated()))
                .andExpect(jsonPath("$[0].items[0].id")
                        .value(itemRequestDto.getItems().get(0).getId()))
                .andExpect(jsonPath("$[0].items[0].name")
                        .value(itemRequestDto.getItems().get(0).getName()));
    }

    @Test
    void getRequestById() throws Exception {
        when(itemRequestService.getRequestById(1L, 1L)).thenReturn(itemRequestDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$.requester.id")
                        .value(itemRequestDto.getRequester().getId()))
                .andExpect(jsonPath("$.requester.name")
                        .value(itemRequestDto.getRequester().getName()))
                .andExpect(jsonPath("$.created").value(itemRequestDto.getCreated()))
                .andExpect(jsonPath("$.items[0].id")
                        .value(itemRequestDto.getItems().get(0).getId()))
                .andExpect(jsonPath("$.items[0].name")
                        .value(itemRequestDto.getItems().get(0).getName()));
    }

    @Test
    void getRequestByIdEntityNotFoundException() throws Exception {
        when(itemRequestService.getRequestById(1L, 1L)).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void addRequest() throws Exception {
        when(itemRequestService.addRequest(1L, itemRequestDtoAdd)).thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDtoAdd))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$.requester.id")
                        .value(itemRequestDto.getRequester().getId()))
                .andExpect(jsonPath("$.requester.name")
                        .value(itemRequestDto.getRequester().getName()))
                .andExpect(jsonPath("$.created").value(itemRequestDto.getCreated()))
                .andExpect(jsonPath("$.items[0].id")
                        .value(itemRequestDto.getItems().get(0).getId()))
                .andExpect(jsonPath("$.items[0].name")
                        .value(itemRequestDto.getItems().get(0).getName()));
    }
}
