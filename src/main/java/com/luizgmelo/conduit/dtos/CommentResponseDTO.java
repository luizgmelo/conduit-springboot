package com.luizgmelo.conduit.dtos;

import com.luizgmelo.conduit.models.Comment;

public record CommentResponseDTO(CommentDTO comment) {
    public static CommentResponseDTO fromComment(Comment comment) {
        return new CommentResponseDTO(new CommentDTO(comment.getId(), comment.getCreatedAt(), comment.getUpdatedAt(),
                                                     comment.getBody(), AuthorDTO.fromAuthor(comment.getAuthor())));
    }
}
