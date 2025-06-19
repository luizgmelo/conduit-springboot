package com.luizgmelo.conduit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

public record ExceptionMessage(HttpStatus status, String field, String message) {
    public ExceptionMessage(FieldError fieldError) {
        this(HttpStatus.BAD_REQUEST, fieldError.getField(), fieldError.getDefaultMessage());
    }
}
