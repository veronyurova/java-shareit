package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class Item {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    @NotNull
    private User owner;
    private ItemRequest request;
}
