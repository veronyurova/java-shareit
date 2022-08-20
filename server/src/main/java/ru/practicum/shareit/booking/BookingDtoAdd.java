package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class BookingDtoAdd {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
