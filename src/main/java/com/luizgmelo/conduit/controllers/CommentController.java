package com.luizgmelo.conduit.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.luizgmelo.conduit.dtos.CommentDTO;
import com.luizgmelo.conduit.dtos.CommentResponseDTO;
import com.luizgmelo.conduit.dtos.MultipleCommentResponseDTO;
import com.luizgmelo.conduit.exceptions.ArticleNotFoundException;
import com.luizgmelo.conduit.exceptions.CommentNotFoundException;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  public CommentController(CommentService commentService, ArticleService articleService) {
    this.commentService = commentService;
    this.articleService = articleService;
  }

  @GetMapping
  public ResponseEntity<MultipleCommentResponseDTO> listComments(@PathVariable String slug) {
    Article article = articleService.getArticle(slug);
    List<CommentDTO> list = new ArrayList<>();

    if (article == null) {
      throw new ArticleNotFoundException();
    }

    Set<Comment> comments = commentService.getAllComments(article);

    if (!comments.isEmpty()) {
      list = comments.stream().map(CommentDTO::fromComment).toList();
    }

    MultipleCommentResponseDTO response = new MultipleCommentResponseDTO(list);
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

    User user = UserService.getAuthenticatedUser();
    if (user == null) {
      throw new UserDetailsFailedException();
    }


    if (!user.getEmail().equalsIgnoreCase(article.getAuthor().getEmail()) &&
        !user.getEmail().equalsIgnoreCase(comment.getAuthor().getEmail())) {
      throw new OperationNotAllowedException();
    }

    commentService.deleteComment(slug, comment);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }


}
