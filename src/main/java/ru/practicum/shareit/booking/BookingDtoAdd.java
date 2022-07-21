package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookingDtoAdd {
    private Long itemId;
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    @NotNull
    @Future
    private LocalDateTime end;
}
