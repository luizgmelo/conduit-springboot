package com.luizgmelo.conduit.exceptions;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException exception) {
    List<String> errors = exception.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
        .collect(Collectors.toList());
    return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = ArticleConflictException.class)
  public ResponseEntity<String> handleArticleConflictException(ArticleConflictException exception) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
  }

  @ExceptionHandler(value = CommentNotFoundException.class)
  public ResponseEntity<String> handleArticleConflictException(CommentNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }


  private Map<String, List<String>> getErrorsMap(List<String> errors) {
    Map<String, List<String>> errorResponse = new HashMap<>();
    errorResponse.put("errors", errors);
    return errorResponse;
  }
}
