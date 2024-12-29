package com.luizgmelo.conduit.exceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
        super("Comment not found!");
    }
}
