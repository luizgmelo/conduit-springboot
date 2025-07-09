package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.dtos.ArticleDTO;
import com.luizgmelo.conduit.dtos.ArticleResponseDTO;
import com.luizgmelo.conduit.dtos.MultipleArticleResponseDTO;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.ArticleRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ArticleQueryService {

    private final ArticleRepository articleRepository;
    private final FavoriteService favoriteService;
    private final FollowService followService;
    private final UserService userService;
    private final ArticleService articleService;

    public ArticleQueryService(ArticleRepository articleRepository, FavoriteService favoriteService, FollowService followService, UserService userService, ArticleService articleService) {
        this.articleRepository = articleRepository;
        this.favoriteService = favoriteService;
        this.followService = followService;
        this.userService = userService;
        this.articleService = articleService;
    }

    public MultipleArticleResponseDTO listArticles(User user, String tag, String author, String favorited, int limit, int offset) {
        Page<Article> articlesPage = findArticlesByCriteria(tag, author, favorited, limit, offset);

        Page<ArticleDTO> list = enrichAndMapArticles(articlesPage, user);

        return new MultipleArticleResponseDTO(list);
    }

    public MultipleArticleResponseDTO feedArticles(User user, int limit, int offset) {
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Article> articlesPage = articleRepository.findArticlesByFollowedUsers(user.getId(), pageable);

        Page<ArticleDTO> list = enrichAndMapArticles(articlesPage, user);

        return new MultipleArticleResponseDTO(list);
    }

    public ArticleResponseDTO getArticle(User user, String slug) {
        Article article = articleService.getArticleBySlug(slug);

        boolean isFavorite = favoriteService.isFavorite(user, article);
        boolean isFollowedAuthor = followService.isFollowing(user, article.getAuthor());

        return ArticleResponseDTO.fromArticle(article, isFavorite, isFollowedAuthor);
    }

    private Page<ArticleDTO> enrichAndMapArticles(Page<Article> articlesPage, User user) {
        List<Article> articles = articlesPage.getContent();

        if (articles.isEmpty() || user == null) {
            return Page.empty(articlesPage.getPageable());
        }

        List<UUID> articleIds = articles.stream().map(Article::getId).toList();
        Set<UUID> authorIds = articles.stream().map(article -> article.getAuthor().getId()).collect(Collectors.toSet());

        Set<UUID> favoritedArticlesIds = favoriteService.findFavoritedArticlesIds(user, articleIds);
        Set<UUID> followedAuthorIds = followService.findFollowedUserIds(user, authorIds);

        List<ArticleDTO> articlesDto = articles.stream().map(article -> {
            boolean isFavorited = favoritedArticlesIds.contains(article.getId());
            boolean isFollowing = followedAuthorIds.contains(article.getAuthor().getId());
            return MultipleArticleResponseDTO.fromArticle(article, isFavorited, isFollowing);
        }).toList();

        return new PageImpl<>(articlesDto, articlesPage.getPageable(), articlesPage.getTotalElements());
    }

    private Page<Article> findArticlesByCriteria(String tag, String author, String favorited, int limit, int offset) {
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (tag != null) {
            return articleRepository.findByTag(tag, pageable);
        } else if (author != null) {
            return articleRepository.findByAuthor(author, pageable);
        } else if (favorited != null) {
            User userWhoFavorite = userService.getUserByUsername(favorited);
            return articleRepository.findFavoritedByUser(userWhoFavorite.getId(), pageable);
        } else {
            return articleRepository.findAll(pageable);
        }
    }
}
