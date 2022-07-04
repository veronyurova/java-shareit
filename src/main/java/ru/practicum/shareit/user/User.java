package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private long id;
    private String name;
    // TODO unique
    private String email;
}
