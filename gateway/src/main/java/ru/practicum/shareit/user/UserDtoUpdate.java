package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class UserDtoUpdate {
    private Long id;
    private String name;
    @Email
    private String email;
}
