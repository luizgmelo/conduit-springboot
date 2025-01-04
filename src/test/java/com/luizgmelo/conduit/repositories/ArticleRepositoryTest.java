package com.luizgmelo.conduit.repositories;

import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.Follow;
import com.luizgmelo.conduit.models.Tag;
import com.luizgmelo.conduit.models.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    @DisplayName("Should get Article successfully by slug from DB")
    void findBySlug_Success() {
        User author = this.createUser();
        Article article = this.createArticle(author);

        Optional<Article> expected = articleRepository.findBySlug(article.getSlug());

        assertThat(expected.isPresent()).isTrue();
        assertThat(expected.get()).isEqualTo(article);
    }

    @Test
    @DisplayName("Should not get Article by slug because it do not exists in DB")
    void findBySlug_Failed() {
        String slug = "slug";

        Optional<Article> expected = articleRepository.findBySlug(slug);

        assertThat(expected.isPresent()).isFalse();
        assertThat(expected).isEmpty();
    }


    @Test
    @DisplayName("Should get Article successfully by tag from DB")
    void findByTag_Success() {
        User author = this.createUser();
        Article article = this.createArticle(author);

        List<Article> expected = articleRepository.findByTag("tag");

        assertThat(expected).isNotEmpty();
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.getFirst()).isEqualTo(article);
    }

    @Test
    @DisplayName("Should not get Article by tag because it do not exist in DB")
    void findByTag_Failed() {
        String tag = "tag";

        List<Article> expected = articleRepository.findByTag(tag);

        assertThat(expected).isEmpty();
    }

    @Test
    @DisplayName("Should get a list of Article successfully by author username from DB")
    void findByAuthor_Success() {
        User author = this.createUser();
        Article article = this.createArticle(author);

        List<Article> expected = articleRepository.findByAuthor("username");

        assertThat(expected).isNotEmpty();
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.getFirst().getAuthor()).isEqualTo(article.getAuthor());
    }

    @Test
    @DisplayName("Should not get Article by author username because it do not exist in DB")
    void findByAuthor_Failed() {
        List<Article> expected = articleRepository.findByAuthor("username");

        assertThat(expected).isEmpty();
    }


    @Test
    @DisplayName("Should get a list of articles favorited by user authenticated from DB")
    void findFavoritedByUser() {
        User author = this.createUser();
        Article article = this.createArticle(author);

        User authenticated = this.createUser("authenticated", "authenticatedEmail");

        authenticated.getFavoriteArticles().add(article);

        List<Article> expected = articleRepository.findFavoritedByUser(authenticated.getUsername());

        assertThat(expected.getFirst()).isEqualTo(article);
        assertThat(expected.getFirst().getAuthor()).isEqualTo(author);
        assertThat(expected.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should get a page of Articles from authors that user authenticated follow from DB")
    void findArticlesByFollowedUsers() {
        User author = this.createUser();
        Article article = this.createArticle(author);

        User authenticated = this.createUser("authenticated", "authenticatedEmail");

        this.createFollow(authenticated, author);

        Pageable pageable = PageRequest.of(0, 1);

        Page<Article> expected = articleRepository.findArticlesByFollowedUsers(authenticated.getId(), pageable);

        assertThat(expected.getContent().getFirst()).isEqualTo(article);
        assertThat(expected.getContent().getFirst().getAuthor()).isEqualTo(author);
        assertThat(expected.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should delete Article successfully from DB")
    void deleteBySlug() {
        User author = this.createUser();
        Article article = this.createArticle(author);

        articleRepository.deleteBySlug(article.getSlug());

        Article removedArticle = entityManager.find(Article.class, article.getId());
        assertThat(removedArticle).isNull();
    }


    private User createUser(String username, String email) {
        User newUser = new User(username, email, "password");

        entityManager.persist(newUser);
        return newUser;
    }

    private User createUser() {
        return this.createUser("username", "email");
    }


    private Article createArticle(User author) {
        Article newArticle = new Article("slug", "title", "description", "body", Set.of(new Tag("tag")), author);

        entityManager.persist(newArticle);
        return newArticle;
    }

    private void createFollow(User follower, User followed) {
        Follow follow = new Follow(follower, followed);

        entityManager.persist(follow);
    }
}
