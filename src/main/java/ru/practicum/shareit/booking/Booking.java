package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class Booking {
    private long id;
    private LocalDate start;
    private LocalDate end;
    private long item;
    private long booker;
    private String status;
}
