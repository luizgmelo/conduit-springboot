package com.luizgmelo.conduit.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.luizgmelo.conduit.models.Article;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
  Optional<Article> findBySlug(String slug);

  @Query("SELECT a FROM Article a JOIN a.tags t WHERE t.name = :tag")
  Page<Article> findByTag(String tag, Pageable pageable);

  @Query("SELECT a FROM Article a WHERE a.author.username = :author")
  Page<Article> findByAuthor(String author, Pageable pageable);

  @Query("SELECT a FROM Article a JOIN a.usersWhoFavorited f WHERE f.user.id = :userWhoFavoritedId")
  Page<Article> findFavoritedByUser(UUID userWhoFavoritedId, Pageable pageable);

  @Query("SELECT a FROM Article a " +
         "JOIN a.author u " +
         "JOIN u.followers f " +
         "WHERE f.follower.id = :userId")
  Page<Article> findArticlesByFollowedUsers(UUID userId, Pageable pageable);

  void deleteBySlug(String slug);
}
