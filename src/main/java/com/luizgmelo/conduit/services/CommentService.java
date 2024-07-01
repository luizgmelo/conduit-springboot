package com.luizgmelo.conduit.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizgmelo.conduit.dtos.CommentDto;
import com.luizgmelo.conduit.models.Comment;
import com.luizgmelo.conduit.repositories.CommentRepository;

@Service
public class CommentService {

  @Autowired
  CommentRepository commentRepository;

  public List<Comment> getAllComments() {
    return commentRepository.findAll();
  }

  public Comment createComment(CommentDto comment) {
    var newComment = new Comment();
    newComment.setBody(comment.body());
    return commentRepository.save(newComment);
  }

  public void deleteComment(UUID id) {
    commentRepository.deleteById(id);
  }

}
