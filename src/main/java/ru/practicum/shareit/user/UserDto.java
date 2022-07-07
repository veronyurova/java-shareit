package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.AllArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    @Email
    private String email;
}
