package com.luizgmelo.conduit.repositories;

import com.luizgmelo.conduit.models.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.luizgmelo.conduit.util.Creations.*;
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
        User author = createUser(entityManager);
        Article article = createArticle(author, entityManager);

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
        User author = createUser(entityManager);
        Article article = createArticle(author, entityManager);
        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());

        Page<Article> expected = articleRepository.findByTag("tag", pageable);

        assertThat(expected).isNotEmpty();
        assertThat(expected.getContent().size()).isEqualTo(1);
        assertThat(expected.getContent().getFirst()).isEqualTo(article);
    }

    @Test
    @DisplayName("Should not get Article by tag because it do not exist in DB")
    void findByTag_Failed() {
        String tag = "tag";
        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());

        Page<Article> expected = articleRepository.findByTag(tag, pageable);

        assertThat(expected).isEmpty();
    }

    @Test
    @DisplayName("Should get a list of Article successfully by author username from DB")
    void findByAuthor_Success() {
        User author = createUser(entityManager);
        Article article = createArticle(author, entityManager);
        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());

        Page<Article> expected = articleRepository.findByAuthor("username", pageable);

        assertThat(expected).isNotEmpty();
        assertThat(expected.getContent().size()).isEqualTo(1);
        assertThat(expected.getContent().getFirst().getAuthor()).isEqualTo(article.getAuthor());
    }

    @Test
    @DisplayName("Should not get Article by author username because it do not exist in DB")
    void findByAuthor_Failed() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());

        Page<Article> expected = articleRepository.findByAuthor("username", pageable);

        assertThat(expected).isEmpty();
    }


    @Test
    @DisplayName("Should get a list of articles favorited by user authenticated from DB")
    void findFavoritedByUser() {
        User author = createUser(entityManager);
        Article article = createArticle(author, entityManager);

        User authenticated = createUser("authenticated", "authenticatedEmail", entityManager);

        createFavorite(authenticated, article, entityManager);

        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());


        Page<Article> expected = articleRepository.findFavoritedByUser(authenticated.getId(), pageable);

        assertThat(expected.getContent().getFirst()).isEqualTo(article);
        assertThat(expected.getContent().getFirst().getAuthor()).isEqualTo(author);
        assertThat(expected.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should get a page of Articles from authors that user authenticated follow from DB")
    void findArticlesByFollowedUsers() {
        User author = createUser(entityManager);
        Article article = createArticle(author, entityManager);

        User authenticated = createUser("authenticated", "authenticatedEmail", entityManager);

        createFollow(authenticated, author, entityManager);

        Pageable pageable = PageRequest.of(0, 1);

        Page<Article> expected = articleRepository.findArticlesByFollowedUsers(authenticated.getId(), pageable);

        assertThat(expected.getContent().getFirst()).isEqualTo(article);
        assertThat(expected.getContent().getFirst().getAuthor()).isEqualTo(author);
        assertThat(expected.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should delete Article successfully from DB")
    void deleteBySlug() {
        User author = createUser(entityManager);
        Article article = createArticle(author, entityManager);

        articleRepository.deleteBySlug(article.getSlug());

        Article removedArticle = entityManager.find(Article.class, article.getId());
        assertThat(removedArticle).isNull();
    }
}
