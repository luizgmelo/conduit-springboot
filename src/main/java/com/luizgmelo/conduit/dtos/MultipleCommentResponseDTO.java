package com.luizgmelo.conduit.dtos;

import com.luizgmelo.conduit.models.Comment;
import java.util.List;

public record MultipleCommentResponseDTO(List<Comment> comments) {
}
