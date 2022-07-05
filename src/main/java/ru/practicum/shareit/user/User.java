package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    @Email
    private String email;
}
