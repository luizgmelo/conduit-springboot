package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.dtos.ArticleResponseDTO;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.Favorite;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.FavoriteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final FollowService followService;
    private final ArticleService articleService;

    public FavoriteService(FavoriteRepository favoriteRepository, FollowService followService, ArticleService articleService) {
        this.favoriteRepository = favoriteRepository;
        this.followService = followService;
        this.articleService = articleService;
    }

    public Set<UUID> findFavoritedArticlesIds(User user, List<UUID> articleIds) {
        if (user == null || articleIds.isEmpty()) {
            return Collections.emptySet();
        }
        return favoriteRepository.findArticleIdsFavoritedByUser(user.getId(), articleIds);
    }

    @Transactional
    public ArticleResponseDTO addFavorite(User user, String slug) {
        Article article = articleService.getArticleBySlug(slug);

        if (!isFavorite(user, article)) {
            article.incrementFavoritesCount();
            Favorite favorite = new Favorite(user, article);
            favoriteRepository.save(favorite);
        }

        boolean isFollowedAuthor = followService.isFollowing(user, article.getAuthor());
        return ArticleResponseDTO.fromArticle(article, true, isFollowedAuthor);
    }

    @Transactional
    public ArticleResponseDTO removeFavorite(User user, String slug) {
        Article article = articleService.getArticleBySlug(slug);

        if (isFavorite(user, article)) {
            article.decrementFavoritesCount();
            favoriteRepository.deleteByUserIdAndArticleId(user.getId(), article.getId());
        }

        boolean isFollowedAuthor = followService.isFollowing(user, article.getAuthor());
        return ArticleResponseDTO.fromArticle(article, false, isFollowedAuthor);
    }

    public boolean isFavorite(User user, Article article) {
        return favoriteRepository.isFavorited(user.getId(), article.getId());
    }
}
