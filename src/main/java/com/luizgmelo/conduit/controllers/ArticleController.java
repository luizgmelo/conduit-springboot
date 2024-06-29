package com.luizgmelo.conduit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.luizgmelo.conduit.dtos.ArticleDto;
import com.luizgmelo.conduit.dtos.ArticleUpdateDto;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.services.ArticleService;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

  @Autowired
  ArticleService articleService;

  @GetMapping("/{slug}")
  public ResponseEntity getArticle(@PathVariable("slug") String slug) {
    Article article = articleService.getArticle(slug);
    if (article != null)
      return ResponseEntity.status(HttpStatus.OK).body(article);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article not found");

  }

  @PostMapping
  public ResponseEntity createArticle(@RequestBody ArticleDto body) {
    Article article = articleService.createNewArticle(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(article);
  }

  @PutMapping("/{slug}")
  public ResponseEntity updateArticle(@PathVariable("slug") String slug, @RequestBody ArticleUpdateDto body) {
    Article updated = articleService.updateArticle(slug, body);
    if (updated != null)
      return ResponseEntity.status(HttpStatus.OK).body(updated);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article not found");
  }

  @DeleteMapping("/{slug}")
  public ResponseEntity deleteArticle(@PathVariable("slug") String slug) {
    Article removed = articleService.removeArticle(slug);
    if (removed != null)
      return ResponseEntity.status(HttpStatus.OK).body(removed);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article not found");
  }
}