package com.luizgmelo.conduit.repositories;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.luizgmelo.conduit.models.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
// TODO Fix Queries
public interface ArticleRepository extends JpaRepository<Article, UUID> {
  Optional<Article> findBySlug(String slug);

  @Query("SELECT a FROM Article a JOIN a.tags t WHERE t.name = :tag")
  List<Article> findByTag(@Param("tag") String tag);

  @Query("SELECT a FROM Article a WHERE a.author.username = :author")
  List<Article> findByAuthor(String author);

  @Query("SELECT a FROM Article a")
  List<Article> findFavoritedByUser(@Param("favorited") String favorited);

  @Query("SELECT a FROM Article a")
  Page<Article> findArticlesByFollowedUsers(UUID userId, Pageable pageable);

  void deleteBySlug(String slug);
}
