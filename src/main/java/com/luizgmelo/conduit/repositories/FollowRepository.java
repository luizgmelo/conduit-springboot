package com.luizgmelo.conduit.repositories;

import com.luizgmelo.conduit.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<Follow, UUID> {
    @Query("""
        SELECT f.followed.id
        FROM Follow f
        WHERE f.follower.id = :followerId AND f.followed.id IN :authorIds
    """)
    Set<UUID> findFollowedUserIds(@Param("followerId") UUID userId, @Param("authorIds") Set<UUID> articleIds);

    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM Follow f WHERE f.follower.id = :followerId AND f.followed.id = :followedId) THEN true ELSE false END")
    boolean isFollowing(UUID followerId, UUID followedId);

    @Modifying
    @Query("DELETE FROM Follow f WHERE f.follower.id = :followerId AND f.followed.id = :followedId")
    void deleteFollowerIdAndFollowedId(UUID followerId, UUID followedId);
}
