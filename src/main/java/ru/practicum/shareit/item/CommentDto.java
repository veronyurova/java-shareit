package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.AllArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotNull
    @NotBlank
    private String text;
    private String authorName;
    private LocalDateTime created;
}
