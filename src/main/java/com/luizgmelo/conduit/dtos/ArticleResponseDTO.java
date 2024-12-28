package com.luizgmelo.conduit.dtos;

import com.luizgmelo.conduit.models.Article;

public record ArticleResponseDTO(ArticleDTO article) {
    public static ArticleResponseDTO fromArticle(Article article) {
        return new ArticleResponseDTO(
                new ArticleDTO(article.getSlug(), article.getTitle(), article.getDescription(), article.getBody(),
                               article.getTagList(), article.getCreatedAt(), article.getUpdatedAt(), article.isFavorite(),
                               article.getFavoritesCount(),
                               new AuthorDTO(article.getAuthor().getUsername(), article.getAuthor().getBio(), article.getAuthor().getImage(),
                                             false)
                )
        );
    }
}
