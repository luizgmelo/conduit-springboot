package com.luizgmelo.conduit.util;

import com.luizgmelo.conduit.models.*;
import jakarta.persistence.EntityManager;

import java.util.Set;

public class Creations {

    public static User createUser(String username, String email, EntityManager entityManager) {
        User newUser = new User(username, email, "password");

        entityManager.persist(newUser);
        return newUser;
    }

    public static User createUser(EntityManager entityManager) {
        return createUser("username", "email", entityManager);
    }


    public static Article createArticle(User author, EntityManager entityManager) {
        Article newArticle = new Article("slug", "title", "description", "body", Set.of(new Tag("tag")), author);

        entityManager.persist(newArticle);
        return newArticle;
    }

    public static void createFollow(User follower, User followed, EntityManager entityManager) {
        Follow follow = new Follow(follower, followed);

        entityManager.persist(follow);
    }

    public static void createFavorite(User user, Article article, EntityManager entityManager) {
        Favorite favorite = new Favorite(user, article);

        entityManager.persist(favorite);
    }

}
