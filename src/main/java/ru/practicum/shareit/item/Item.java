package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class Item {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private long owner;
    private long request;
}
