package com.luizgmelo.conduit.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record RequestArticleDTO(
                @NotBlank String title,
                @NotBlank String description,
                @NotBlank String body,
                List<String> tagList) {
}
