package com.luizgmelo.conduit.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RequestArticleDTO(
        @NotBlank(message = "Title must not be blank")
        @Size(min = 5, max = 50, message = "Title size must be between 5 and 50")
        String title,

        @NotBlank(message = "Description must not be blank")
        @Size(min = 5, max = 100, message = "Description size must be between 5 and 100")
        String description,

        @NotBlank(message = "Body must not be blank") @Size(min = 5, max = 250, message = "Body size must be between 5 and 250")
        String body,

        List<
        @NotBlank(message = "Tag must not be blank") @Size(max = 10, message = "Tag max capacity 10 tags")
                String> tagList) {}
