package com.luizgmelo.conduit.controllers;

import java.util.List;

import com.luizgmelo.conduit.dtos.ResponseArticleDTO;
import com.luizgmelo.conduit.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizgmelo.conduit.dtos.RequestArticleDTO;
import com.luizgmelo.conduit.dtos.ArticleUpdateDto;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.services.ArticleService;

// TODO Each endpoint should receive a Dto Request and Return a Dto Response.
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

  @Autowired
  ArticleService articleService;

  @GetMapping
  public ResponseEntity<List<Article>> getListArticles() {
    List<Article> articles = articleService.listArticles();
    return ResponseEntity.status(HttpStatus.OK).body(articles);
  }

  @GetMapping("/{slug}")
  public ResponseEntity getArticle(@PathVariable("slug") String slug) {
    Article article = articleService.getArticle(slug);
    if (article != null)
      return ResponseEntity.status(HttpStatus.OK).body(article);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article not found");

  }

  @PostMapping
  public ResponseEntity<ResponseArticleDTO> createArticle(@RequestBody RequestArticleDTO request) {
    User user = getAuthenticatedUser();
    if (user == null) {
      throw new RuntimeException("Error: Missing authentication details");
    }
    Article article = articleService.createNewArticle(request, user);
    ResponseArticleDTO response = ResponseArticleDTO.fromArticle(article);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{slug}")
  public ResponseEntity updateArticle(@PathVariable("slug") String slug, @RequestBody ArticleUpdateDto body) {
    // TODO check if who is updating is the author
    Article updated = articleService.updateArticle(slug, body);
    if (updated != null)
      return ResponseEntity.status(HttpStatus.OK).body(updated);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article not found");
  }

  @DeleteMapping("/{slug}")
  public ResponseEntity deleteArticle(@PathVariable("slug") String slug) {
    articleService.removeArticle(slug);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  private User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    if (principal instanceof User) {
      return (User) principal;
    }
    return null;
  }
}