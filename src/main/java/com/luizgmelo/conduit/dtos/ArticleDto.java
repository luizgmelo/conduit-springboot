package com.luizgmelo.conduit.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;

public record ArticleDto(
                @NotBlank String title,
                @NotBlank String description,
                @NotBlank String body,
                ArrayList<String> tagList) {
}
