package com.luizgmelo.conduit.controllers;

import java.util.UUID;

import com.luizgmelo.conduit.dtos.CommentResponseDTO;
import com.luizgmelo.conduit.dtos.MultipleCommentResponseDTO;
import com.luizgmelo.conduit.exceptions.ArticleNotFoundException;
import com.luizgmelo.conduit.exceptions.CommentNotFoundException;
import com.luizgmelo.conduit.exceptions.OperationNotAllowedException;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.services.UserService;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.luizgmelo.conduit.dtos.CommentDto;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.Comment;
import com.luizgmelo.conduit.services.ArticleService;
import com.luizgmelo.conduit.services.CommentService;

@RestController
@RequestMapping("/api/articles/{slug}/comments")
public class CommentController {

  private final CommentService commentService;

  private final ArticleService articleService;

  private final UserService userService;

  public CommentController(CommentService commentService, ArticleService articleService, UserService userService) {
    this.commentService = commentService;
    this.articleService = articleService;
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<MultipleCommentResponseDTO> listComments(@PathVariable String slug,
                                                                 @RequestParam(defaultValue = "0") @Range(min = 0, max = 10, message = "Page number must between 0 and 10") int page,
                                                                 @RequestParam(defaultValue = "10") @Range(min = 0, max = 10, message = "Size number must between 0 and 10") int size) {
    Article article = articleService.getArticle(slug);

    if (article == null) {
      throw new ArticleNotFoundException();
    }

    Page<Comment> comments = commentService.getAllComments(article, page, size);

    MultipleCommentResponseDTO response = new MultipleCommentResponseDTO(comments.getContent());
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping
  public ResponseEntity<CommentResponseDTO> sendComment(@PathVariable("slug") String slug, @RequestBody CommentDto commentDto) {
    Article currentArticle = articleService.getArticle(slug);
    Comment comment = commentService.createComment(currentArticle, commentDto);
    CommentResponseDTO response = CommentResponseDTO.fromComment(comment);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> removeComment(@PathVariable(value = "slug") String slug, @PathVariable(value = "id") UUID commentId) {
    Article article = articleService.getArticle(slug);
    if (article == null) {
      throw new ArticleNotFoundException();
    }

    Comment comment = commentService.getCommentById(commentId);
    if (comment == null) {
      throw new CommentNotFoundException();
    }

    User user = userService.getAuthenticatedUser();

    if (!user.getEmail().equalsIgnoreCase(article.getAuthor().getEmail()) &&
        !user.getEmail().equalsIgnoreCase(comment.getAuthor().getEmail())) {
      throw new OperationNotAllowedException();
    }

    commentService.deleteComment(slug, comment);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }


}
