package com.luizgmelo.conduit.controllers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  CommentService commentService;

  @Autowired
  ArticleService articleService;

  @GetMapping
  public ResponseEntity listComments(@PathVariable("slug") String slug) {
    Set<Comment> comments = commentService.getAllComments(slug);
    if (!comments.isEmpty())
      return ResponseEntity.status(HttpStatus.OK).body(comments);
    return ResponseEntity.status(HttpStatus.OK).body("This article does not have comment yet.");
  }

  @PostMapping
  public ResponseEntity sendComment(@PathVariable("slug") String slug, @RequestBody CommentDto commentDto) {
    Article currentArticle = articleService.getArticle(slug);
    Comment comment = commentService.createComment(currentArticle, commentDto);
    return ResponseEntity.status(HttpStatus.OK).body(comment);
  }

  @DeleteMapping("{id}")
  public ResponseEntity removeComment(@PathVariable("id") UUID id) {
    commentService.deleteComment(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
