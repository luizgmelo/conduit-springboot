package com.luizgmelo.conduit.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizgmelo.conduit.dtos.ArticleDto;
import com.luizgmelo.conduit.dtos.ArticleUpdateDto;
import com.luizgmelo.conduit.exceptions.ArticleNotFoundException;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.repositories.ArticleRepository;

@Service
public class ArticleService {

  @Autowired
  ArticleRepository articleRepository;

  public Article getArticle(String slug) {
    Optional<Article> articleOpt = articleRepository.findBySlug(slug);
    if (articleOpt.isPresent()) {
      return articleOpt.get();
    }
    throw new ArticleNotFoundException("Article not found!");
  }

  public Article createNewArticle(ArticleDto data) {
    Article newArticle = new Article(data.title(), data.description(), data.body(), data.tagList());
    return articleRepository.save(newArticle);
  }

  public Article updateArticle(String slug, ArticleUpdateDto data) {
    Article articleOld = this.getArticle(slug);
    if (articleOld != null) {
      articleOld.setTitle(data.title());
      articleOld.setDescription(data.description());
      articleOld.setBody(data.body());
      articleOld.setFavorited(data.favorited());
      articleOld.setAuthor(data.author());
      var articleUpdated = articleRepository.save(articleOld);
      return articleUpdated;
    }
    return null;

  }

  public Article removeArticle(String slug) {
    Article removed = this.getArticle(slug);
    if (removed != null)
      articleRepository.delete(removed);
    return removed;
  }
}
