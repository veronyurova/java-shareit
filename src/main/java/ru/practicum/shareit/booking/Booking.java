package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Future;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class Booking {
    private Long id;
    @NotNull
    @FutureOrPresent
    private LocalDate start;
    @NotNull
    @Future
    private LocalDate end;
    @NotNull
    private Item item;
    @NotNull
    private User booker;
    @NotNull
    private String status;
}
