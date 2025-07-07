package com.luizgmelo.conduit.services;

import com.github.slugify.Slugify;
import com.luizgmelo.conduit.dtos.*;
import com.luizgmelo.conduit.exceptions.ArticleConflictException;
import com.luizgmelo.conduit.exceptions.ArticleNotFoundException;
import com.luizgmelo.conduit.exceptions.OperationNotAllowedException;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.Tag;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final TagService tagService;

  public ArticleService(ArticleRepository articleRepository, TagService tagService) {
    this.articleRepository = articleRepository;
      this.tagService = tagService;
  }

  public Article getArticleBySlug(String slug) {
    return articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
  }

  public ArticleResponseDTO createNewArticle(RequestArticleDTO data, User author) {
    Slugify slugify = Slugify.builder().build();
    String slug = slugify.slugify(data.article().title());

    Set<Tag> tags = new HashSet<>();

    for (String name : data.article().tagList()) {
      Tag tag = tagService.getOrSaveTagByName(name);
      tags.add(tag);
    }

    Optional<Article> articleOpt = articleRepository.findBySlug(slug);
    if (articleOpt.isPresent()) {
      throw new ArticleConflictException();
    }

    Article newArticle = new Article(slug, data.article().title(), data.article().description(), data.article().body(), tags, author);

    Article savedArticle = articleRepository.save(newArticle);

    return ArticleResponseDTO.fromArticle(savedArticle, false, false);
  }

  public Article updateArticle(User user, String slug,RequestUpdateArticleDto data) {
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

    return articleRepository.save(articleOld);
  }

  @Transactional
  public void removeArticle(String slug) {
    articleRepository.findBySlug(slug).orElseThrow(ArticleNotFoundException::new);
    articleRepository.deleteBySlug(slug);
  }
}
