package com.luizgmelo.conduit.exceptions;

import java.time.ZonedDateTime;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionPayload {
  private final String message;
  private final HttpStatus httpStatus;
  private final ZonedDateTime timestamp;

  public ExceptionPayload(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
    this.message = message;
    this.httpStatus = httpStatus;
    this.timestamp = timestamp;
  }

}