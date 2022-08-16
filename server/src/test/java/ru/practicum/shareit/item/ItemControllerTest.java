package ru.practicum.shareit.item;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.ValidationException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private final ItemDto itemDto = new ItemDto(1L, "Item 1", "Test", true,
            new ItemDto.UserDto(1L, "User"), null, null, null, null);
    private final ItemDto itemDtoAdd = new ItemDto(null, "Item 1", "Test", true,
            null, null, null, null, null);
    private final CommentDto commentDto = new CommentDto(1L, "Comment", "User", null);
    private final CommentDto commentDtoAdd = new CommentDto(null, "Comment", "User", null);

    @Test
    void getOwnerItems() throws Exception {
        when(itemService.getOwnerItems(1L, 1, 1)).thenReturn(List.of(itemDto));

        mvc.perform(get("/items?from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].owner.id").value(itemDto.getOwner().getId()))
                .andExpect(jsonPath("$[0].owner.name").value(itemDto.getOwner().getName()))
                .andExpect(jsonPath("$[0].requestId").value(itemDto.getRequestId()))
                .andExpect(jsonPath("$[0].lastBooking").value(itemDto.getLastBooking()))
                .andExpect(jsonPath("$[0].nextBooking").value(itemDto.getNextBooking()))
                .andExpect(jsonPath("$[0].comments").value(itemDto.getComments()));
    }

    @Test
    void getOwnerItemsDefaultPaginationParameters() throws Exception {
        when(itemService.getOwnerItems(1L, 0, 10)).thenReturn(List.of(itemDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].owner.id").value(itemDto.getOwner().getId()))
                .andExpect(jsonPath("$[0].owner.name").value(itemDto.getOwner().getName()))
                .andExpect(jsonPath("$[0].requestId").value(itemDto.getRequestId()))
                .andExpect(jsonPath("$[0].lastBooking").value(itemDto.getLastBooking()))
                .andExpect(jsonPath("$[0].nextBooking").value(itemDto.getNextBooking()))
                .andExpect(jsonPath("$[0].comments").value(itemDto.getComments()));
    }

    @Test
    void searchItems() throws Exception {
        when(itemService.searchItems("test", 1, 1)).thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search?text=test&from=1&size=1")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].owner.id").value(itemDto.getOwner().getId()))
                .andExpect(jsonPath("$[0].owner.name").value(itemDto.getOwner().getName()))
                .andExpect(jsonPath("$[0].requestId").value(itemDto.getRequestId()))
                .andExpect(jsonPath("$[0].lastBooking").value(itemDto.getLastBooking()))
                .andExpect(jsonPath("$[0].nextBooking").value(itemDto.getNextBooking()))
                .andExpect(jsonPath("$[0].comments").value(itemDto.getComments()));
    }

    @Test
    void searchItemsDefaultPaginationParameters() throws Exception {
        when(itemService.searchItems("test", 0, 10)).thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search?text=test")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].owner.id").value(itemDto.getOwner().getId()))
                .andExpect(jsonPath("$[0].owner.name").value(itemDto.getOwner().getName()))
                .andExpect(jsonPath("$[0].requestId").value(itemDto.getRequestId()))
                .andExpect(jsonPath("$[0].lastBooking").value(itemDto.getLastBooking()))
                .andExpect(jsonPath("$[0].nextBooking").value(itemDto.getNextBooking()))
                .andExpect(jsonPath("$[0].comments").value(itemDto.getComments()));
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemById(1L, 1L)).thenReturn(itemDto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.owner.id").value(itemDto.getOwner().getId()))
                .andExpect(jsonPath("$.owner.name").value(itemDto.getOwner().getName()))
                .andExpect(jsonPath("$.requestId").value(itemDto.getRequestId()))
                .andExpect(jsonPath("$.lastBooking").value(itemDto.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(itemDto.getNextBooking()))
                .andExpect(jsonPath("$.comments").value(itemDto.getComments()));
    }

    @Test
    void getItemByIdEntityNotFoundException() throws Exception {
        when(itemService.getItemById(1L, 1L)).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void addUser() throws Exception {
        when(itemService.addItem(1L, itemDtoAdd)).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDtoAdd))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.owner.id").value(itemDto.getOwner().getId()))
                .andExpect(jsonPath("$.owner.name").value(itemDto.getOwner().getName()))
                .andExpect(jsonPath("$.requestId").value(itemDto.getRequestId()))
                .andExpect(jsonPath("$.lastBooking").value(itemDto.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(itemDto.getNextBooking()))
                .andExpect(jsonPath("$.comments").value(itemDto.getComments()));
    }

    @Test
    void updateUser() throws Exception {
        when(itemService.updateItem(1L, 1L, itemDto)).thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.owner.id").value(itemDto.getOwner().getId()))
                .andExpect(jsonPath("$.owner.name").value(itemDto.getOwner().getName()))
                .andExpect(jsonPath("$.requestId").value(itemDto.getRequestId()))
                .andExpect(jsonPath("$.lastBooking").value(itemDto.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(itemDto.getNextBooking()))
                .andExpect(jsonPath("$.comments").value(itemDto.getComments()));
    }

    @Test
    void updateUserEntityNotFoundException() throws Exception {
        when(itemService.updateItem(1L, 1L, itemDto)).thenThrow(EntityNotFoundException.class);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserAccessDeniedException() throws Exception {
        when(itemService.updateItem(1L, 1L, itemDto)).thenThrow(AccessDeniedException.class);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUserById() throws Exception {
        mvc.perform(delete("/items/1").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void addComment() throws Exception {
        when(itemService.addComment(1L, 1L, commentDtoAdd)).thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDtoAdd))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()))
                .andExpect(jsonPath("$.created").value(commentDto.getCreated()));
    }

    @Test
    void addCommentValidationException() throws Exception {
        when(itemService.addComment(1L, 1L, commentDtoAdd)).thenThrow(ValidationException.class);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDtoAdd))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }
}
