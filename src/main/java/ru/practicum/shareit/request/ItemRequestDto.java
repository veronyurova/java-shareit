package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.AllArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private User requestor;
    @FutureOrPresent
    private LocalDateTime created;

    @Getter
    @AllArgsConstructor
    static class User {
        private Long id;
        private String name;
    }
}
