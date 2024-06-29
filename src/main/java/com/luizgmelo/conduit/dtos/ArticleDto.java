package com.luizgmelo.conduit.dtos;

import jakarta.validation.constraints.NotBlank;

public record ArticleDto(
                @NotBlank String title,
                @NotBlank String description,
                @NotBlank String body,
                String[] tagList) {
}
