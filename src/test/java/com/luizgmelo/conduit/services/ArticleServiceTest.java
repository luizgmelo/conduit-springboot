package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.dtos.RequestArticleDTO;
import com.luizgmelo.conduit.exceptions.ArticleConflictException;
import com.luizgmelo.conduit.exceptions.ArticleNotFoundException;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.models.Tag;
import com.luizgmelo.conduit.models.User;
import com.luizgmelo.conduit.repositories.ArticleRepository;
import com.luizgmelo.conduit.repositories.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private TagRepository tagRepository;

    // CONSTANTS
    private static final User USER = new User("username", "email", "password");
    private static final Article ARTICLE = new Article("title", "title", "description", "body", Set.of(new Tag("tag")), USER);

    @Test
    @DisplayName("Should return Article successfully from DB")
    void getArticleSuccess() {
        Article article = ARTICLE;
        String slug = "slug";
        when(articleRepository.findBySlug(slug)).thenReturn(Optional.of(article));

        Article expected = articleService.getArticle(slug);

        assertThat(expected).isEqualTo(article);
    }

    @Test
    @DisplayName("Should throw Exception when Article is not found")
    void getArticleFailed() {
        String slug = "slug";

        when(articleRepository.findBySlug(slug)).thenReturn(Optional.empty());

        ArticleNotFoundException thrown = Assertions.assertThrows(ArticleNotFoundException.class, () -> {
            articleService.getArticle(slug);
        });

        Assertions.assertEquals("Article not found!", thrown.getMessage());
    }

    @Test
    @DisplayName("Should create a new Article in DB")
    void createNewArticleTestSuccess() {
        RequestArticleDTO data = new RequestArticleDTO(ARTICLE.getTitle(), ARTICLE.getDescription(),
                                                          ARTICLE.getBody(), ARTICLE.getTagList());

        when(articleRepository.findBySlug(ARTICLE.getSlug())).thenReturn(Optional.empty());
        when(articleRepository.save(any(Article.class))).thenReturn(ARTICLE);

        Article expected = articleService.createNewArticle(data, USER);

        assertThat(expected).isEqualTo(ARTICLE);
    }

    @Test
    @DisplayName("Should throws exception when create a Article that already exists in DB")
    void createNewArticleTestFailed() {
        RequestArticleDTO data = new RequestArticleDTO(ARTICLE.getTitle(), ARTICLE.getDescription(),
                ARTICLE.getBody(), ARTICLE.getTagList());

        when(articleRepository.findBySlug(ARTICLE.getSlug())).thenReturn(Optional.empty());
        when(articleRepository.save(any(Article.class))).thenReturn(ARTICLE);

        articleService.createNewArticle(data, USER);

        when(articleRepository.findBySlug(ARTICLE.getSlug())).thenReturn(Optional.of(ARTICLE));

        assertThatThrownBy(() -> articleService.createNewArticle(data, USER)).isInstanceOf(ArticleConflictException.class);
    }

}