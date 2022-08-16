package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private final BookingDto bookingDto = new BookingDto(1L, null, null,
            new BookingDto.ItemDto(1L, "Item 1"), new BookingDto.UserDto(1L, "User"),
            BookingStatus.WAITING);
    private final BookingDtoAdd bookingDtoAdd = new BookingDtoAdd(1L, null, null);

    @Test
    void getRequesterBookings() throws Exception {
        when(bookingService.getRequesterBookings(1L, "TEST", 1, 1))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings?state=TEST&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].start").value(bookingDto.getStart()))
                .andExpect(jsonPath("$[0].end").value(bookingDto.getEnd()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].booker.name").value(bookingDto.getBooker().getName()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()));
    }

    @Test
    void getRequesterBookingsDefaultState() throws Exception {
        when(bookingService.getRequesterBookings(1L, "ALL", 1, 1))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings?from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].start").value(bookingDto.getStart()))
                .andExpect(jsonPath("$[0].end").value(bookingDto.getEnd()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].booker.name").value(bookingDto.getBooker().getName()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()));
    }

    @Test
    void getRequesterBookingsDefaultPaginationParameters() throws Exception {
        when(bookingService.getRequesterBookings(1L, "TEST", 0, 10))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings?state=TEST")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].start").value(bookingDto.getStart()))
                .andExpect(jsonPath("$[0].end").value(bookingDto.getEnd()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].booker.name").value(bookingDto.getBooker().getName()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()));
    }

    @Test
    void getOwnerBookings() throws Exception {
        when(bookingService.getOwnerBookings(1L, "TEST", 1, 1))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner?state=TEST&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].start").value(bookingDto.getStart()))
                .andExpect(jsonPath("$[0].end").value(bookingDto.getEnd()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].booker.name").value(bookingDto.getBooker().getName()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()));
    }

    @Test
    void getOwnerBookingsDefaultState() throws Exception {
        when(bookingService.getOwnerBookings(1L, "ALL", 1, 1))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner?from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].start").value(bookingDto.getStart()))
                .andExpect(jsonPath("$[0].end").value(bookingDto.getEnd()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].booker.name").value(bookingDto.getBooker().getName()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()));
    }

    @Test
    void getOwnerBookingsDefaultPaginationParameters() throws Exception {
        when(bookingService.getOwnerBookings(1L, "TEST", 0, 10))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner?state=TEST")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].start").value(bookingDto.getStart()))
                .andExpect(jsonPath("$[0].end").value(bookingDto.getEnd()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].booker.name").value(bookingDto.getBooker().getName()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()));
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingService.getBookingById(1L, 1L)).thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.start").value(bookingDto.getStart()))
                .andExpect(jsonPath("$.end").value(bookingDto.getEnd()))
                .andExpect(jsonPath("$.item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$.booker.name").value(bookingDto.getBooker().getName()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()));
    }

    @Test
    void getBookingByIdEntityNotFoundException() throws Exception {
        when(bookingService.getBookingById(1L, 1L)).thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void getAddBooking() throws Exception {
        when(bookingService.addBooking(1L, 1L, bookingDtoAdd)).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingDtoAdd))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.start").value(bookingDto.getStart()))
                .andExpect(jsonPath("$.end").value(bookingDto.getEnd()))
                .andExpect(jsonPath("$.item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$.booker.name").value(bookingDto.getBooker().getName()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()));
    }

    @Test
    void getUpdateBookingStatus() throws Exception {
        when(bookingService.updateBookingStatus(1L, 1L, true)).thenReturn(bookingDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.start").value(bookingDto.getStart()))
                .andExpect(jsonPath("$.end").value(bookingDto.getEnd()))
                .andExpect(jsonPath("$.item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$.booker.name").value(bookingDto.getBooker().getName()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()));
    }

    @Test
    void getUpdateBookingStatusIncorrectStatus() throws Exception {
        mvc.perform(patch("/bookings/1?approved=incorrect")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }
}
