package com.luizgmelo.conduit.services;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.luizgmelo.conduit.models.Article;

public class QueryBuilder {
  private QueryBuilder() {

  }

  public static Example<Article> makeQuery(Article article) {
    ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();
    return Example.of(article, exampleMatcher);
  }
}