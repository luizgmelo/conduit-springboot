package com.luizgmelo.conduit.dtos;

import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.User;

import java.time.LocalDateTime;
import java.util.List;

// TODO Should use AuthorDTO and not Author entity
public record ResponseArticleDTO(String slug, String title, String description, String body, List<String> tagList,
                                 LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isFavorite, Integer favoritesCount, User author) {

    public static ResponseArticleDTO fromArticle(Article article) {
        return new ResponseArticleDTO(article.getSlug(), article.getTitle(), article.getDescription(),
                article.getBody(), article.getTagList(), article.getCreatedAt(),
                article.getUpdatedAt(), article.isFavorite(), article.getFavoritesCount(),
                article.getAuthor());
    }
}
