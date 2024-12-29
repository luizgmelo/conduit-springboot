package com.luizgmelo.conduit.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.github.slugify.Slugify;
import com.luizgmelo.conduit.exceptions.ArticleConflictException;
import com.luizgmelo.conduit.models.User;
import org.springframework.stereotype.Service;

import com.luizgmelo.conduit.dtos.RequestArticleDTO;
import com.luizgmelo.conduit.dtos.ArticleUpdateDto;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.repositories.ArticleRepository;

@Service
public class ArticleService {

  private final ArticleRepository articleRepository;

  public ArticleService(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  public List<Article> listArticles() {
    return articleRepository.findAll();
  }

  public Article getArticle(String slug) {
    Optional<Article> articleOpt = articleRepository.findBySlug(slug);
    return articleOpt.orElse(null);
  }

  public Article createNewArticle(RequestArticleDTO data, User author) {
    List<String> tagList = Arrays.asList(data.tagList());
    Slugify slugify = Slugify.builder().build();
    String slug = slugify.slugify(data.title());

    if (this.getArticle(slug) != null) {
      throw new ArticleConflictException();
    }

    Article newArticle = new Article(slug, data.title(), data.description(), data.body(), tagList, author);

    return articleRepository.save(newArticle);
  }

  public Article updateArticle(Article articleOld, ArticleUpdateDto data) {
    Slugify slugify = Slugify.builder().build();

    articleOld.setSlug(slugify.slugify(data.title()));
    articleOld.setTitle(data.title());
    articleOld.setDescription(data.description());
    articleOld.setBody(data.body());
    articleOld.setTagList(data.tagList());

    return articleRepository.save(articleOld);
  }

  public void removeArticle(String slug) {
    articleRepository.deleteBySlug(slug);
  }
}
