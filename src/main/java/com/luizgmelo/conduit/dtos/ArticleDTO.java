package com.luizgmelo.conduit.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luizgmelo.conduit.models.Article;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleDTO(String slug, String title, String description, @JsonInclude(JsonInclude.Include.NON_NULL) String body, List<String> tagList,
                         LocalDateTime createdAt, LocalDateTime updatedAt, boolean favorited, int favoritesCount,
                         AuthorDTO author) {
}
