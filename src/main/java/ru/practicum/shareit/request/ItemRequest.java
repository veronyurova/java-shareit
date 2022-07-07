package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ItemRequest {
    private Long id;
    @NotNull
    @NotBlank
    private String description;
    private User requestor;
    @NotNull
    @FutureOrPresent
    private LocalDateTime created;
}
