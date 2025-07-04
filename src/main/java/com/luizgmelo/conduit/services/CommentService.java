package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.dtos.CommentDTO;
import com.luizgmelo.conduit.dtos.CommentRequestDto;
import com.luizgmelo.conduit.dtos.CommentResponseDTO;
import com.luizgmelo.conduit.dtos.MultipleCommentResponseDTO;
import com.luizgmelo.conduit.exceptions.CommentNotFoundException;
import com.luizgmelo.conduit.exceptions.OperationNotAllowedException;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.Comment;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final ArticleService articleService;

  public CommentService(CommentRepository commentRepository, ArticleService articleService) {
    this.commentRepository = commentRepository;
    this.articleService = articleService;
  }

  public Comment getCommentById(UUID commentId) {
    return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
  }

  public MultipleCommentResponseDTO getAllComments(String slug, int page, int size) {
    Article article = articleService.getArticleBySlug(slug);
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<Comment> comments = commentRepository.findByCommentFromId(article.getId(), pageable);
    List<CommentDTO> commentDTOList = comments.getContent().stream().map(CommentDTO::fromComment).toList();
    return new MultipleCommentResponseDTO(commentDTOList);
  }

  public CommentResponseDTO createComment(String slug, User user,CommentRequestDto commentDto) {
    Article article = articleService.getArticleBySlug(slug);

    String comment = commentDto.comment().body();

    Comment newComment = new Comment(comment, article, user);

    Comment commentSaved = commentRepository.save(newComment);
    return CommentResponseDTO.fromComment(commentSaved);
  }

  public void deleteComment(User user, String slug, UUID commentId) {
    Article article = articleService.getArticleBySlug(slug);
    Comment comment = this.getCommentById(commentId);

    if (!user.getEmail().equalsIgnoreCase(article.getAuthor().getEmail())) {
      throw new OperationNotAllowedException();
    }

    if (comment.getCommentFrom().getSlug().equals(slug)) {
      commentRepository.delete(comment);
    }
  }
}
