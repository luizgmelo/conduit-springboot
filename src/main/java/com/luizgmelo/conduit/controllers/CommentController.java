package com.luizgmelo.conduit.controllers;

import com.luizgmelo.conduit.dtos.CommentRequestDto;
import com.luizgmelo.conduit.dtos.CommentResponseDTO;
import com.luizgmelo.conduit.dtos.MultipleCommentResponseDTO;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/api/articles/{slug}/comments")
public class CommentController {

  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @GetMapping
  public ResponseEntity<MultipleCommentResponseDTO> listComments(@PathVariable String slug,
                                                                 Pageable pageable) {
    return ResponseEntity.ok(commentService.getAllComments(slug, pageable));
  }

  @PostMapping
  public ResponseEntity<CommentResponseDTO> sendComment(@PathVariable("slug") String slug,
                                                        @AuthenticationPrincipal User user,
                                                        @Valid @RequestBody CommentRequestDto commentDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(slug, user, commentDto));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> removeComment(@AuthenticationPrincipal User user,
                                            @PathVariable(value = "slug") String slug,
                                            @PathVariable(value = "id") UUID commentId) {
    commentService.deleteComment(user, slug, commentId);
    return ResponseEntity.noContent().build();
  }
}
