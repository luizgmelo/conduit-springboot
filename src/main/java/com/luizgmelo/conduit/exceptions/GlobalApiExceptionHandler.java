package com.luizgmelo.conduit.exceptions;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class  GlobalApiExceptionHandler {
  @ExceptionHandler(value = { ArticleNotFoundException.class, UserProfileNotFoundException.class,
      UserNotFoundException.class })
  public ResponseEntity<Object> handleApiRequestException(ArticleNotFoundException exception) {
    ExceptionPayload apiException = new ExceptionPayload(exception.getMessage(), HttpStatus.NOT_FOUND,
        ZonedDateTime.now(ZoneId.of("America/Recife")));

    return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<List<ExceptionMessage>> handleValidationException(MethodArgumentNotValidException exception) {
    List<ExceptionMessage> errors = exception.getBindingResult().getFieldErrors().stream().map(ExceptionMessage::new)
        .collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(value = ArticleConflictException.class)
  public ResponseEntity<ExceptionPayload> handleArticleConflictException(ArticleConflictException exception) {
    ExceptionPayload apiException = new ExceptionPayload(exception.getMessage(), HttpStatus.CONFLICT,
            ZonedDateTime.now(ZoneId.of("America/Recife")));

      return ResponseEntity.status(HttpStatus.CONFLICT).body(apiException);
  }

  @ExceptionHandler(value = CommentNotFoundException.class)
  public ResponseEntity<ExceptionPayload> handleCommentNotFoundException(CommentNotFoundException exception) {
    ExceptionPayload apiException = new ExceptionPayload(exception.getMessage(), HttpStatus.NOT_FOUND,
            ZonedDateTime.now(ZoneId.of("America/Recife")));

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiException);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
  }
}
