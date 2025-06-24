package com.luizgmelo.conduit.dtos;

import jakarta.validation.constraints.NotBlank;

public record CommentDto(@NotBlank String body) {
}
