package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.dtos.ArticleResponseDTO;
import com.luizgmelo.conduit.exceptions.ArticleNotFoundException;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.Favorite;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.ArticleRepository;
import com.luizgmelo.conduit.repositories.FavoriteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final ArticleRepository articleRepository;
    private final FavoriteRepository favoriteRepository;
    private final FollowService followService;

    public FavoriteService(ArticleRepository articleRepository, FavoriteRepository favoriteRepository, FollowService followService) {
        this.articleRepository = articleRepository;
        this.favoriteRepository = favoriteRepository;
        this.followService = followService;
    }

    public ArticleResponseDTO addFavorite(User user, String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(ArticleNotFoundException::new);

        if (!isFavorite(user, article)) {
            article.setFavoritesCount(article.getFavoritesCount() + 1);
            Favorite favorite = new Favorite(user, article);
            favoriteRepository.save(favorite);
        }

        boolean isFavorite = true;
        boolean isFollowedAuthor = followService.isFollowing(user, article.getAuthor());
        return ArticleResponseDTO.fromArticle(article, isFavorite, isFollowedAuthor);
    }

    @Transactional
    public ArticleResponseDTO removeFavorite(User user, String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(ArticleNotFoundException::new);

        if (isFavorite(user, article)) {
            article.setFavoritesCount(article.getFavoritesCount() - 1);
            favoriteRepository.deleteByUserIdAndArticleId(user.getId(), article.getId());
        }

        boolean isFavorite = false;
        boolean isFollowedAuthor = followService.isFollowing(user, article.getAuthor());
        return ArticleResponseDTO.fromArticle(article, isFavorite, isFollowedAuthor);
    }

    public boolean isFavorite(User user, Article article) {
        return favoriteRepository.isFavorited(user.getId(), article.getId());
    }
}
