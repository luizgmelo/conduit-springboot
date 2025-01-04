package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.exceptions.ArticleNotFoundException;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.Favorite;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.ArticleRepository;
import com.luizgmelo.conduit.repositories.FavoriteRepository;
import com.luizgmelo.conduit.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final ArticleRepository articleRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(UserRepository userRepository, ArticleRepository articleRepository, FavoriteRepository favoriteRepository) {
        this.articleRepository = articleRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public Article addFavorite(User user, String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(ArticleNotFoundException::new);

        if (!isFavorite(user, article)) {
            article.setFavoritesCount(article.getFavoritesCount() + 1);
            Favorite favorite = new Favorite(user, article);
            favoriteRepository.save(favorite);
        }

        return article;
    }

    @Transactional
    public Article removeFavorite(User user, String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(ArticleNotFoundException::new);

        if (isFavorite(user, article)) {
            article.setFavoritesCount(article.getFavoritesCount() - 1);
            favoriteRepository.deleteByUserIdAndArticleId(user.getId(), article.getId());
        }

        return article;
    }

    public boolean isFavorite(User user, Article article) {
        return favoriteRepository.isFavorited(user.getId(), article.getId());
    }
}
