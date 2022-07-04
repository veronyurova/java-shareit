package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ItemRequest {
    private long id;
    private String description;
    private long requestor;
    private LocalDate created;
}
