package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
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
