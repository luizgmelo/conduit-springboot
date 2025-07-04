package com.luizgmelo.conduit.services;

import java.util.*;

import com.github.slugify.Slugify;
import com.luizgmelo.conduit.dtos.*;
import com.luizgmelo.conduit.exceptions.ArticleConflictException;
import com.luizgmelo.conduit.exceptions.ArticleNotFoundException;
import com.luizgmelo.conduit.exceptions.OperationNotAllowedException;
import com.luizgmelo.conduit.exceptions.UserNotFoundException;
import com.luizgmelo.conduit.models.Tag;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.TagRepository;
import com.luizgmelo.conduit.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.repositories.ArticleRepository;

@Service
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final TagRepository tagRepository;
  private final UserRepository userRepository;
  private final FollowService followService;
  private final FavoriteService favoriteService;

  public ArticleService(ArticleRepository articleRepository, TagRepository tagRepository, UserRepository userRepository, FollowService followService, FavoriteService favoriteService) {
    this.articleRepository = articleRepository;
    this.tagRepository = tagRepository;
    this.userRepository = userRepository;
    this.followService = followService;
    this.favoriteService = favoriteService;
  }

  public MultipleArticleResponseDTO listArticles(User user, String tag, String author, String favorited, int limit, int offset) {
    Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
    List<Article> articles;

    if (tag != null) {
      articles = articleRepository.findByTag(tag, pageable).getContent();
    } else if (author != null) {
      articles = articleRepository.findByAuthor(author, pageable).getContent();
    } else if (favorited != null) {
      User userWhoFavorite = userRepository.findByUsername(favorited).orElseThrow(() -> new UserNotFoundException("User who favorited not found"));
      articles = articleRepository.findFavoritedByUser(userWhoFavorite.getId(), pageable).getContent();
    } else {
      articles = articleRepository.findAll(pageable).getContent();
    }

    List<ArticleDTO> list = articles.stream().map(article -> MultipleArticleResponseDTO.fromArticle(article,
            favoriteService.isFavorite(user, article),
            followService.isFollowing(user, article.getAuthor()))).toList();

    return new MultipleArticleResponseDTO(list);
  }

  public MultipleArticleResponseDTO feedArticles(User user, int limit, int offset) {
    Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

    List<Article> articles = articleRepository.findArticlesByFollowedUsers(user.getId(), pageable).getContent();

    List<ArticleDTO> list = articles.stream().map(article -> MultipleArticleResponseDTO.fromArticle(article,
            favoriteService.isFavorite(user, article),
            followService.isFollowing(user, article.getAuthor()))).toList();

    return new MultipleArticleResponseDTO(list);

  }

  public Article getArticleBySlug(String slug) {
    return articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
  }

  public ArticleResponseDTO getArticle(User user, String slug) {
    Optional<Article> articleOpt = articleRepository.findBySlug(slug);
    Article article = articleOpt.orElseThrow(ArticleNotFoundException::new);

    boolean isFavorite = favoriteService.isFavorite(user, article);
    boolean isFollowedAuthor = followService.isFollowing(user, article.getAuthor());

    return ArticleResponseDTO.fromArticle(article, isFavorite, isFollowedAuthor);
  }

  public ArticleResponseDTO createNewArticle(RequestArticleDTO data, User author) {
    Slugify slugify = Slugify.builder().build();
    String slug = slugify.slugify(data.article().title());
    Set<Tag> tags = buildTags(data.article().tagList());

    Optional<Article> articleOpt = articleRepository.findBySlug(slug);
    if (articleOpt.isPresent()) {
      throw new ArticleConflictException();
    }

    Article newArticle = new Article(slug, data.article().title(), data.article().description(), data.article().body(), tags, author);

    Article savedArticle = articleRepository.save(newArticle);

    return ArticleResponseDTO.fromArticle(savedArticle, false, false);
  }

  public ArticleResponseDTO updateArticle(User user, String slug,RequestUpdateArticleDto data) {

    Optional<Article> articleOpt = articleRepository.findBySlug(slug);
    Article articleOld = articleOpt.orElseThrow(ArticleNotFoundException::new);

    if (!user.getEmail().equals(articleOld.getAuthor().getEmail())) {
      throw new OperationNotAllowedException();
    }

    Slugify slugify = Slugify.builder().build();

    if (data.title() != null) {
      articleOld.setSlug(slugify.slugify(data.title()));
      articleOld.setTitle(data.title());
    }

    if (data.description() != null) {
      articleOld.setDescription(data.description());
    }

    if (data.body() != null) {
      articleOld.setBody(data.body());
    }

    Article updated = articleRepository.save(articleOld);

    boolean isFavorite = favoriteService.isFavorite(user, updated);
    boolean isFollowedAuthor = followService.isFollowing(user, updated.getAuthor());

    return ArticleResponseDTO.fromArticle(updated, isFavorite, isFollowedAuthor);
  }

  @Transactional
  public void removeArticle(String slug) {
    articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
    articleRepository.deleteBySlug(slug);
  }

  private Set<Tag> buildTags(List<String> tagList) {
    Set<Tag> tags = new HashSet<>();

    for (String name : tagList) {
      Tag tag = tagRepository.findByName(name)
              .orElseGet(() -> tagRepository.save(new Tag(name)));
      tags.add(tag);
    }

    return tags;
  }
}
