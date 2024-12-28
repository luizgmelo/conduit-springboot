package com.luizgmelo.conduit.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleDTO(String slug, String title, String description, String body, List<String> tagList,
                         LocalDateTime createdAt, LocalDateTime updatedAt, boolean favorited, int favoritesCount,
                         AuthorDTO author) {
}
