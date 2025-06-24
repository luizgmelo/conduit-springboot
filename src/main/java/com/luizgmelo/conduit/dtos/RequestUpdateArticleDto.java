package com.luizgmelo.conduit.dtos;

import jakarta.validation.constraints.Size;

public record RequestUpdateArticleDto(
        @Size(min = 5, max = 50, message = "Title size must be between 5 and 50")
        String title,

        @Size(min = 5, max = 100, message = "Description size must be between 5 and 100")
        String description,

        @Size(min = 5, max = 250, message = "Body size must be between 5 and 250")
        String body
) {
}
