package com.luizgmelo.conduit.controllers;

import java.util.List;

import com.luizgmelo.conduit.dtos.*;
import com.luizgmelo.conduit.exceptions.ArticleNotFoundException;
import com.luizgmelo.conduit.exceptions.OperationNotAllowedException;
import com.luizgmelo.conduit.exceptions.UserDetailsFailedException;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.services.ArticleService;

// TODO Each endpoint should receive a Dto Request and Return a Dto Response.
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

  private final ArticleService articleService;

  public ArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @GetMapping
  public ResponseEntity<List<ArticleResponseDTO>> getListArticles() {
    List<Article> articles = articleService.listArticles();
    List<ArticleResponseDTO> response = articles.stream().map(ArticleResponseDTO::fromArticle).toList();
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{slug}")
  public ResponseEntity<ArticleResponseDTO> getArticle(@PathVariable("slug") String slug) {
    Article article = articleService.getArticle(slug);
    ArticleResponseDTO response = ArticleResponseDTO.fromArticle(article);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping
  public ResponseEntity<ArticleResponseDTO> createArticle(@RequestBody RequestArticleDTO dto) {
    User user = UserService.getAuthenticatedUser();
    if (user == null) {
      throw new UserDetailsFailedException();
    }
    Article article = articleService.createNewArticle(dto, user);
    ArticleResponseDTO response = ArticleResponseDTO.fromArticle(article);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{slug}")
  public ResponseEntity<ArticleResponseDTO> updateArticle(@PathVariable("slug") String slug, @RequestBody ArticleUpdateDto dto) {
    User user = UserService.getAuthenticatedUser();

    if (user == null) {
      throw new UserDetailsFailedException();
    }

    Article articleOld = articleService.getArticle(slug);

    if (articleOld == null) {
      throw new ArticleNotFoundException();
    }

    if (!user.getEmail().equals(articleOld.getAuthor().getEmail())) {
      throw new OperationNotAllowedException();
    }

    Article updated = articleService.updateArticle(articleOld, dto);
    ArticleResponseDTO response = ArticleResponseDTO.fromArticle(updated);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{slug}")
  public ResponseEntity<Void> deleteArticle(@PathVariable("slug") String slug) {
    articleService.removeArticle(slug);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}