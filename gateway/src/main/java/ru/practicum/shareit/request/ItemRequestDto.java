package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.practicum.shareit.item.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotNull
    @NotBlank
    private String description;
    private UserDto requester;
    private LocalDateTime created;
    private List<ItemDto> items;

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    static class UserDto {
        private Long id;
        private String name;
    }
}
