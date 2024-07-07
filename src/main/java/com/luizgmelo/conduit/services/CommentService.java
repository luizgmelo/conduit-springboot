package com.luizgmelo.conduit.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizgmelo.conduit.dtos.CommentDto;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.Comment;
import com.luizgmelo.conduit.repositories.ArticleRepository;
import com.luizgmelo.conduit.repositories.CommentRepository;

@Service
public class CommentService {

  @Autowired
  ArticleService articleService;

  @Autowired
  CommentRepository commentRepository;

  @Autowired
  ArticleRepository articleRepository;

  public Set<Comment> getAllComments(String slug) {
    Article article = articleService.getArticle(slug);

    return article.getComments();
  }

  public Comment createComment(Article article, CommentDto commentDto) {
    String comment = commentDto.body();

    Comment newComment = new Comment(comment, article);

    return commentRepository.save(newComment);
  }

  public void deleteComment(UUID id) {
    commentRepository.deleteById(id);
  }

}
