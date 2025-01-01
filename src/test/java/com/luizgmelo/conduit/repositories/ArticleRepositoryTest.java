package com.luizgmelo.conduit.repositories;

import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.Tag;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.models.UserProfile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
    private TestEntityManager testEntityManager;

    private static final User USER = new User("username", "email", "password");
    private static final User FOLLOWED = new User("followed", "followed_email", "password");
    private static final UserProfile USER_PROFILE = new UserProfile(USER);
    private static final UserProfile FOLLOWED_PROFILE = new UserProfile(FOLLOWED);
    private static final Article ARTICLE = new Article("slug", "title", "description", "body", Set.of(new Tag("tag")), USER);
    private static final Article ARTICLE_FOLLOWED = new Article("slug", "title", "description", "body", Set.of(new Tag("tag")), FOLLOWED);

    @BeforeEach
    void beforeEach() {
        testEntityManager.persistAndFlush(USER);
    }

    @AfterEach
    void afterEach() {
        testEntityManager.clear();
        USER.setId(null);
        FOLLOWED.setId(null);
    }

    @Test
    void findBySlug_Success() {
        Article article = testEntityManager.persistFlushFind(ARTICLE);

        Optional<Article> expected = articleRepository.findBySlug(ARTICLE.getSlug());

        assertThat(expected.isPresent()).isTrue();
        assertThat(expected.get()).isEqualTo(article);
    }

    @Test
    void findBySlug_Failed() {
        Optional<Article> expected = articleRepository.findBySlug(ARTICLE.getSlug());

        assertThat(expected.isPresent()).isFalse();
        assertThat(expected).isEmpty();
    }

    @Test
    void findByTag_Success() {
        Article article = testEntityManager.persistFlushFind(ARTICLE);

        List<Article> expected = articleRepository.findByTag("tag");

        assertThat(expected).isNotEmpty();
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.getFirst()).isEqualTo(article);
    }

    @Test
    void findByTag_Failed() {
        List<Article> expected = articleRepository.findByTag("tag");

        assertThat(expected).isEmpty();
    }

    @Test
    void findByAuthor_Success() {
        Article article = testEntityManager.persistFlushFind(ARTICLE);

        List<Article> expected = articleRepository.findByAuthor("username");

        assertThat(expected).isNotEmpty();
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.getFirst().getAuthor()).isEqualTo(article.getAuthor());
    }

    @Test
    void findByAuthor_Failed() {
        List<Article> expected = articleRepository.findByAuthor("username");

        assertThat(expected).isEmpty();
    }

    @Test
    void findFavoritedByUser() {
        USER.setProfile(USER_PROFILE);
        Article article = testEntityManager.persistFlushFind(ARTICLE);
        USER.getProfile().getFavoriteArticles().add(ARTICLE);

        List<Article> expected = articleRepository.findFavoritedByUser("username");

        assertThat(expected.getFirst()).isEqualTo(article);
        assertThat(expected.size()).isEqualTo(1);
    }

    @Test
    void findArticlesByFollowedUsers() {
        USER.setProfile(USER_PROFILE);
        testEntityManager.persistAndFlush(FOLLOWED);
        FOLLOWED.setProfile(FOLLOWED_PROFILE);
        USER.getProfile().getFollowing().add(FOLLOWED_PROFILE);
        Pageable pageable = PageRequest.of(0, 2);

        Article article = testEntityManager.persistFlushFind(ARTICLE_FOLLOWED);

        Page<Article> expected = articleRepository.findArticlesByFollowedUsers(USER.getId(), pageable);

        assertThat(expected.getContent().getFirst()).isEqualTo(article);
        assertThat(expected.getContent().getFirst().getAuthor()).isEqualTo(article.getAuthor());
        assertThat(expected.getContent().size()).isEqualTo(1);
    }

    @Test
    void deleteBySlug() {
        Article article = testEntityManager.persistFlushFind(ARTICLE);
        articleRepository.deleteBySlug(ARTICLE.getSlug());

        Article removedArticle = testEntityManager.find(Article.class, article.getId());
        assertThat(removedArticle).isNull();
    }
}
