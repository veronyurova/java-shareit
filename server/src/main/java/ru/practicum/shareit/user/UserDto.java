package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
