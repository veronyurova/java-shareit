package ru.practicum.shareit.item;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(
                null,
                commentDto.getText(),
                null,
                null,
                LocalDateTime.now()
        );
    }
}
