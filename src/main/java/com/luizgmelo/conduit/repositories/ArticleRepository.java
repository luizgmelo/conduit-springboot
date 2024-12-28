package com.luizgmelo.conduit.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luizgmelo.conduit.models.Article;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
  Optional<Article> findBySlug(String slug);
  void deleteBySlug(String slug);
}
