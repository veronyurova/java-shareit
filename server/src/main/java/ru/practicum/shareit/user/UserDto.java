package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    @Email
    private String email;
}
