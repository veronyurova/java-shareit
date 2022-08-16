package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotNull
    @NotBlank
    private String text;
    private String authorName;
    private LocalDateTime created;
}
