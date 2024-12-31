package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.exceptions.ArticleNotFoundException;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.UserProfile;
import com.luizgmelo.conduit.repositories.ArticleRepository;
import com.luizgmelo.conduit.repositories.UserProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final UserProfileRepository userProfileRepository;
    private final ArticleRepository articleRepository;

    public FavoriteService(UserProfileRepository userProfileRepository, ArticleRepository articleRepository) {
        this.userProfileRepository = userProfileRepository;
        this.articleRepository = articleRepository;
    }

    public Article addFavorite(UserProfile userProfile, String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(ArticleNotFoundException::new);

        if (!userProfile.getFavoriteArticles().contains(article)) {
            article.setFavoritesCount(article.getFavoritesCount() + 1);
            userProfile.getFavoriteArticles().add(article);

            userProfileRepository.save(userProfile);
        }

        return article;
    }

    public Article removeFavorite(UserProfile userProfile, String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(ArticleNotFoundException::new);

        if (userProfile.getFavoriteArticles().contains(article)) {
            article.setFavoritesCount(article.getFavoritesCount() - 1);
            userProfile.getFavoriteArticles().remove(article);

            userProfileRepository.save(userProfile);
        }

        return article;
    }
}
