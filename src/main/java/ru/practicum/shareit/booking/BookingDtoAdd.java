package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookingDtoAdd {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
