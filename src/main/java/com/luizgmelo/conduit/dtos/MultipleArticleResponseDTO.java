package com.luizgmelo.conduit.dtos;

import com.luizgmelo.conduit.models.Article;

import java.util.List;

public record MultipleArticleResponseDTO(List<ArticleDTO> articles) {
    public static ArticleDTO fromArticle(Article article, boolean isFavorited, boolean isFollowing) {
        return new ArticleDTO(article.getSlug(), article.getTitle() , article.getDescription(), null,
                    article.getTagList(), article.getCreatedAt(), article.getUpdatedAt(), isFavorited,
                    article.getFavoritesCount(),
                    new AuthorDTO(article.getAuthor().getUsername(), article.getAuthor().getBio(), article.getAuthor().getImage(),
                            isFollowing)
        );
    }
}
