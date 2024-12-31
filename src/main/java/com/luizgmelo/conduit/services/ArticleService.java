package com.luizgmelo.conduit.services;

import java.util.*;

import com.github.slugify.Slugify;
import com.luizgmelo.conduit.exceptions.ArticleConflictException;
import com.luizgmelo.conduit.models.Tag;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.luizgmelo.conduit.dtos.RequestArticleDTO;
import com.luizgmelo.conduit.dtos.ArticleUpdateDto;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.repositories.ArticleRepository;

@Service
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final TagRepository tagRepository;

  public ArticleService(ArticleRepository articleRepository, TagRepository tagRepository) {
    this.articleRepository = articleRepository;
      this.tagRepository = tagRepository;
  }

  public List<Article> listArticles(String tag, String author, String favorited, int limit, int offset) {
    Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

    if (tag != null) {
      return articleRepository.findByTag(tag);
    } else if (author != null) {
      return articleRepository.findByAuthor(author);
    } else if (favorited != null) {
      return articleRepository.findFavoritedByUser(favorited);
    } else {
      return articleRepository.findAll(pageable).getContent();
    }
  }

  public Article getArticle(String slug) {
    Optional<Article> articleOpt = articleRepository.findBySlug(slug);
    return articleOpt.orElse(null);
  }

  public Article createNewArticle(RequestArticleDTO data, User author) {
    Slugify slugify = Slugify.builder().build();
    String slug = slugify.slugify(data.title());
    Set<Tag> tags = buildTags(data.tagList());

    if (this.getArticle(slug) != null) {
      throw new ArticleConflictException();
    }

    Article newArticle = new Article(slug, data.title(), data.description(), data.body(), tags, author);

    return articleRepository.save(newArticle);
  }

  public Article updateArticle(Article articleOld, ArticleUpdateDto data) {
    Slugify slugify = Slugify.builder().build();
    Set<Tag> tags = buildTags(data.tagList());

    articleOld.setSlug(slugify.slugify(data.title()));
    articleOld.setTitle(data.title());
    articleOld.setDescription(data.description());
    articleOld.setBody(data.body());
    articleOld.setTags(tags);

    return articleRepository.save(articleOld);
  }

  @Transactional
  public void removeArticle(String slug) {
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
