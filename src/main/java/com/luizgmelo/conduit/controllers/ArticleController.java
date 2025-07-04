package com.luizgmelo.conduit.controllers;

import com.luizgmelo.conduit.dtos.ArticleResponseDTO;
import com.luizgmelo.conduit.dtos.MultipleArticleResponseDTO;
import com.luizgmelo.conduit.dtos.RequestArticleDTO;
import com.luizgmelo.conduit.dtos.RequestUpdateArticleDto;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.services.ArticleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

  private final ArticleService articleService;

  public ArticleController(ArticleService articleService) {
    this.articleService = articleService;
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
  public ResponseEntity<MultipleArticleResponseDTO> getFeedArticles(@AuthenticationPrincipal User user,
                                                                    @RequestParam(defaultValue = "20") @Min(0) @Max(50) int limit,
                                                                    @RequestParam(defaultValue = "0") int offset) {
    return ResponseEntity.ok(articleService.feedArticles(user, limit, offset));
  }

  @GetMapping("/{slug}")
  public ResponseEntity<ArticleResponseDTO> getArticle(@AuthenticationPrincipal User user,
                                                        @PathVariable("slug") String slug) {
    return ResponseEntity.ok(articleService.getArticle(user, slug));
  }

  @PostMapping
  public ResponseEntity<ArticleResponseDTO> createArticle(@AuthenticationPrincipal User user,
                                                          @Valid @RequestBody RequestArticleDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(articleService.createNewArticle(dto, user));
  }

  @PutMapping("/{slug}")
  public ResponseEntity<ArticleResponseDTO> updateArticle(@AuthenticationPrincipal User user,
                                                          @PathVariable("slug") String slug,
                                                          @Valid @RequestBody RequestUpdateArticleDto dto) {

    return ResponseEntity.ok(articleService.updateArticle(user, slug, dto));
  }

  @DeleteMapping("/{slug}")
  public ResponseEntity<Void> deleteArticle(@PathVariable("slug") String slug) {
    articleService.removeArticle(slug);
    return ResponseEntity.noContent().build();
  }
}