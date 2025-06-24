package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.dtos.CommentRequestDto;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.Comment;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

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


  public Page<Comment> getAllComments(Article article, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    return commentRepository.findByCommentFromId(article.getId(), pageable);
  }

  public Comment createComment(Article article, CommentRequestDto commentDto) {
    String comment = commentDto.comment().body();
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
