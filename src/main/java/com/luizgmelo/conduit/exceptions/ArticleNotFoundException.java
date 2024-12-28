package com.luizgmelo.conduit.exceptions;

public class ArticleNotFoundException extends RuntimeException {
  public ArticleNotFoundException() {
    super("Article not found!");
  }
}