package com.luizgmelo.conduit.exceptions;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalApiExceptionHandler {
  @ExceptionHandler(value = { ArticleNotFoundException.class, UserProfileNotFoundException.class })
  public ResponseEntity<Object> handleApiRequestException(ArticleNotFoundException exception) {
    ExceptionPayload apiException = new ExceptionPayload(exception.getMessage(), exception, HttpStatus.NOT_FOUND,
        ZonedDateTime.now(ZoneId.of("America/Recife")));

    return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
  }
}
