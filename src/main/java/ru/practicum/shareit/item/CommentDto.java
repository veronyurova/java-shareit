package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.AllArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentDto {
    Long id;
    @NotNull
    @NotBlank
    String text;
    String authorName;
    LocalDateTime created;
}
