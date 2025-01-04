package com.luizgmelo.conduit.services;

import java.util.Set;
import java.util.UUID;

import com.luizgmelo.conduit.models.User;
import org.springframework.stereotype.Service;

import com.luizgmelo.conduit.dtos.CommentDto;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.Comment;
import com.luizgmelo.conduit.repositories.CommentRepository;

@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final UserService userService;

  public CommentService(CommentRepository commentRepository, UserService userService) {
    this.commentRepository = commentRepository;
    this.userService = userService;
  }

  public Comment getCommentById(UUID commentId) {
    return commentRepository.findById(commentId).orElse(null);
  }


  public Set<Comment> getAllComments(Article article) {
    return article.getComments();
  }

  public Comment createComment(Article article, CommentDto commentDto) {
    String comment = commentDto.body();
    User author = userService.getAuthenticatedUser();

    Comment newComment = new Comment(comment, article, author);

    return commentRepository.save(newComment);
  }

  public void deleteComment(String slug, Comment comment) {
    if (comment.getCommentFrom().getSlug().equals(slug)) {
      commentRepository.delete(comment);
    }
  }
}
