package com.luizgmelo.conduit.services;

import java.util.*;

import com.github.slugify.Slugify;
import com.luizgmelo.conduit.dtos.RequestUpdateArticleDto;
import com.luizgmelo.conduit.exceptions.ArticleConflictException;
import com.luizgmelo.conduit.exceptions.ArticleNotFoundException;
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

import com.luizgmelo.conduit.dtos.RequestArticleDTO;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.repositories.ArticleRepository;

@Service
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final TagRepository tagRepository;
  private final UserRepository userRepository;

  public ArticleService(ArticleRepository articleRepository, TagRepository tagRepository, UserRepository userRepository) {
    this.articleRepository = articleRepository;
    this.tagRepository = tagRepository;
    this.userRepository = userRepository;
  }

  public List<Article> listArticles(String tag, String author, String favorited, int limit, int offset) {
    Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

    if (tag != null) {
      return articleRepository.findByTag(tag, pageable).getContent();
    } else if (author != null) {
      return articleRepository.findByAuthor(author, pageable).getContent();
    } else if (favorited != null) {
      User userWhoFavorite = userRepository.findByUsername(favorited).orElseThrow(() -> new UserNotFoundException("User who favorited not found"));
      return articleRepository.findFavoritedByUser(userWhoFavorite.getId(), pageable).getContent();
    } else {
      return articleRepository.findAll(pageable).getContent();
    }
  }

  public List<Article> feedArticles(User userAuthenticated, int limit, int offset) {
    Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

    return articleRepository
            .findArticlesByFollowedUsers(userAuthenticated.getId(), pageable).getContent();
  }

  public Article getArticle(String slug) {
    Optional<Article> articleOpt = articleRepository.findBySlug(slug);
    return articleOpt.orElseThrow(ArticleNotFoundException::new);
  }

  public Article createNewArticle(RequestArticleDTO data, User author) {
    Slugify slugify = Slugify.builder().build();
    String slug = slugify.slugify(data.article().title());
    Set<Tag> tags = buildTags(data.article().tagList());

    Optional<Article> articleOpt = articleRepository.findBySlug(slug);
    if (articleOpt.isPresent()) {
      throw new ArticleConflictException();
    }

    Article newArticle = new Article(slug, data.article().title(), data.article().description(), data.article().body(), tags, author);

    return articleRepository.save(newArticle);
  }

  public Article updateArticle(Article articleOld, RequestUpdateArticleDto data) {
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

    return articleRepository.save(articleOld);
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
