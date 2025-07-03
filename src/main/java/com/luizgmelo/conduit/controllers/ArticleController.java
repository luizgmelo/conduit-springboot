package com.luizgmelo.conduit.controllers;

import java.util.List;

import com.luizgmelo.conduit.dtos.*;
import com.luizgmelo.conduit.exceptions.OperationNotAllowedException;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.services.FavoriteService;
import com.luizgmelo.conduit.services.FollowService;
import com.luizgmelo.conduit.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.services.ArticleService;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

  private final ArticleService articleService;
  private final FavoriteService favoriteService;
  private final UserService userService;
  private final FollowService followService;

  public ArticleController(ArticleService articleService, FavoriteService favoriteService, UserService userService, FollowService followService) {
    this.articleService = articleService;
    this.favoriteService = favoriteService;
    this.userService = userService;
    this.followService = followService;
  }

  @GetMapping
  public ResponseEntity<MultipleArticleResponseDTO> getListArticles(@AuthenticationPrincipal User user,
                                                                  @RequestParam(required = false) String tag,
                                                                  @RequestParam(required = false) String author,
                                                                  @RequestParam(required = false) String favorited,
                                                                  @RequestParam(defaultValue = "20") @Min(0) @Max(50) int limit,
                                                                  @RequestParam(defaultValue = "0") int offset) {
    return ResponseEntity.ok(articleService.listArticles(user, tag, author, favorited, limit, offset));
  }

  @GetMapping("/feed")
  public ResponseEntity<MultipleArticleResponseDTO> getFeedArticles(@RequestParam(defaultValue = "20") @Min(0) @Max(50) int limit,
                                                                    @RequestParam(defaultValue = "0") int offset) {
    User user = userService.getAuthenticatedUser();

    List<Article> articles = articleService.feedArticles(user, limit, offset);
    List<ArticleDTO> list = articles.stream().map(article -> MultipleArticleResponseDTO.fromArticle(article,
            favoriteService.isFavorite(user, article),
            followService.isFollowing(user, article.getAuthor()))).toList();
    MultipleArticleResponseDTO response = new MultipleArticleResponseDTO(list);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{slug}")
  public ResponseEntity<ArticleResponseDTO> getArticle(@PathVariable("slug") String slug) {
    User user = userService.getAuthenticatedUser();
    Article article = articleService.getArticle(slug);
    boolean isFavorite = favoriteService.isFavorite(user, article);
    boolean isFollowedAuthor = followService.isFollowing(user, article.getAuthor());
    ArticleResponseDTO response = ArticleResponseDTO.fromArticle(article, isFavorite, isFollowedAuthor);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<ArticleResponseDTO> createArticle(@Valid @RequestBody RequestArticleDTO dto) {
    User user = userService.getAuthenticatedUser();
    Article article = articleService.createNewArticle(dto, user);
    ArticleResponseDTO response = ArticleResponseDTO.fromArticle(article, false, false);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{slug}")
  public ResponseEntity<ArticleResponseDTO> updateArticle(@PathVariable("slug") String slug, @Valid @RequestBody RequestUpdateArticleDto dto) {
    User user = userService.getAuthenticatedUser();

    Article articleOld = articleService.getArticle(slug);

    if (!user.getEmail().equals(articleOld.getAuthor().getEmail())) {
      throw new OperationNotAllowedException();
    }

    Article updated = articleService.updateArticle(articleOld, dto);
    boolean isFavorite = favoriteService.isFavorite(user, updated);
    boolean isFollowedAuthor = followService.isFollowing(user, updated.getAuthor());
    ArticleResponseDTO response = ArticleResponseDTO.fromArticle(updated, isFavorite, isFollowedAuthor);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{slug}")
  public ResponseEntity<Void> deleteArticle(@PathVariable("slug") String slug) {
    articleService.removeArticle(slug);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping("/{slug}/favorite")
  public ResponseEntity<ArticleResponseDTO> addFavorite(@PathVariable String slug) {
    User user = userService.getAuthenticatedUser();
    Article article = favoriteService.addFavorite(user , slug);
    boolean isFavorite = true;
    boolean isFollowedAuthor = followService.isFollowing(user, article.getAuthor());
    ArticleResponseDTO response = ArticleResponseDTO.fromArticle(article, isFavorite, isFollowedAuthor);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{slug}/favorite")
  public ResponseEntity<ArticleResponseDTO> removeFavorite(@PathVariable String slug) {
    User user = userService.getAuthenticatedUser();
    Article article = favoriteService.removeFavorite(user , slug);
    boolean isFavorite = false;
    boolean isFollowedAuthor = followService.isFollowing(user, article.getAuthor());
    ArticleResponseDTO response = ArticleResponseDTO.fromArticle(article, isFavorite, isFollowedAuthor);
    return ResponseEntity.ok(response);
  }
}