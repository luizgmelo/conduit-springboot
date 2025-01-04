package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.exceptions.ArticleNotFoundException;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.ArticleRepository;
import com.luizgmelo.conduit.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public FavoriteService(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public Article addFavorite(User user, String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(ArticleNotFoundException::new);

        if (!user.getFavoriteArticles().contains(article)) {
            article.setFavoritesCount(article.getFavoritesCount() + 1);
            user.getFavoriteArticles().add(article);

            userRepository.save(user);
        }

        return article;
    }

    public Article removeFavorite(User user, String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(ArticleNotFoundException::new);

        if (user.getFavoriteArticles().contains(article)) {
            article.setFavoritesCount(article.getFavoritesCount() - 1);
            user.getFavoriteArticles().remove(article);

            userRepository.save(user);
        }

        return article;
    }
}
