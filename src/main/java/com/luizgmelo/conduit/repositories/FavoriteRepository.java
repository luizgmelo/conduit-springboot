package com.luizgmelo.conduit.repositories;

import com.luizgmelo.conduit.models.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    @Query("SELECT CASE WHEN EXISTS (SELECT  1 FROM Favorite f WHERE f.user.id = :userId AND f.article.id = :articleId) THEN true ELSE false END")
    boolean isFavorited(UUID userId, UUID articleId);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.user.id = :userId AND f.article.id = :articleId")
    void deleteByUserIdAndArticleId(UUID userId, UUID articleId);

    @Query("""
        SELECT f.article.id
        FROM Favorite f
        WHERE f.user.id = :userId AND f.article.id IN :articleIds
    """)
    Set<UUID> findArticleIdsFavoritedByUser(@Param("userId") UUID userId, @Param("articleIds") List<UUID> articleIds);
}
