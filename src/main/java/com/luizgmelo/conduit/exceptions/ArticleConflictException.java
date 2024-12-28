package com.luizgmelo.conduit.exceptions;

public class ArticleConflictException extends RuntimeException {
    public ArticleConflictException() {
        super("Article already exists.");
    }
}
