package com.luizgmelo.conduit.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.luizgmelo.conduit.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizgmelo.conduit.dtos.RequestArticleDTO;
import com.luizgmelo.conduit.dtos.ArticleUpdateDto;
import com.luizgmelo.conduit.exceptions.ArticleNotFoundException;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.repositories.ArticleRepository;

@Service
public class ArticleService {

  @Autowired
  private ArticleRepository articleRepository;

  public List<Article> listArticles() {
    return articleRepository.findAll();
  }

  public Article getArticle(String slug) {
    Optional<Article> articleOpt = articleRepository.findBySlug(slug);
    if (articleOpt.isPresent()) {
      return articleOpt.get();
    }
    throw new ArticleNotFoundException("Article not found!");
  }

  public Article createNewArticle(RequestArticleDTO data, User author) {
    List<String> tagList = Arrays.asList(data.tagList());
    Article newArticle = new Article(data.title(), data.description(), data.body(), tagList, author);
    return articleRepository.save(newArticle);
  }

  public Article updateArticle(String slug, ArticleUpdateDto data) {
    Article articleOld = this.getArticle(slug);
    if (articleOld != null) {
      articleOld.setTitle(data.title());
      articleOld.setDescription(data.description());
      articleOld.setBody(data.body());
      articleOld.setFavorite(data.favorited());
        return articleRepository.save(articleOld);
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
