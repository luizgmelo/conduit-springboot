package com.luizgmelo.conduit.dtos;

import com.luizgmelo.conduit.models.Comment;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentDTO(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt, String body, AuthorDTO author) {
    public static CommentDTO fromComment(Comment comment) {
        return new CommentDTO(comment.getId(), comment.getCreatedAt(), comment.getUpdatedAt(), comment.getBody(),
                              AuthorDTO.fromAuthor(comment.getAuthor()));
    }
}
