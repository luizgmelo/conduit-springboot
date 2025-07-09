package com.luizgmelo.conduit.services;

import com.luizgmelo.conduit.dtos.ArticleResponseDTO;
import com.luizgmelo.conduit.dtos.CreateArticleDto;
import com.luizgmelo.conduit.dtos.RequestArticleDTO;
import com.luizgmelo.conduit.dtos.RequestUpdateArticleDto;
import com.luizgmelo.conduit.exceptions.ArticleConflictException;
import com.luizgmelo.conduit.exceptions.ArticleNotFoundException;
import com.luizgmelo.conduit.models.Article;
import com.luizgmelo.conduit.repositories.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.luizgmelo.conduit.util.Constants.ARTICLE;
import static com.luizgmelo.conduit.util.Constants.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ArticleServiceTest {

    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private TagService tagService;

    @Test
    @DisplayName("Should create a new Article in DB")
    void createNewArticleTestSuccess() {
        RequestArticleDTO data = new RequestArticleDTO(new CreateArticleDto(ARTICLE.getTitle(), ARTICLE.getDescription(),
                                                          ARTICLE.getBody(), ARTICLE.getTagList()));

        when(articleRepository.findBySlug(ARTICLE.getSlug())).thenReturn(Optional.empty());
        when(articleRepository.save(any(Article.class))).thenReturn(ARTICLE);

        ArticleResponseDTO expected = articleService.createNewArticle(data, USER);

        assertThat(expected.article().slug()).isEqualTo(ARTICLE.getSlug());
    }

    @Test
    @DisplayName("Should throws exception when create a Article that already exists in DB")
    void createNewArticleTestFailed() {
        RequestArticleDTO data = new RequestArticleDTO(new CreateArticleDto(ARTICLE.getTitle(), ARTICLE.getDescription(),
                ARTICLE.getBody(), ARTICLE.getTagList()));

        when(articleRepository.findBySlug(ARTICLE.getSlug())).thenReturn(Optional.empty());
        when(articleRepository.save(any(Article.class))).thenReturn(ARTICLE);

        articleService.createNewArticle(data, USER);

        when(articleRepository.findBySlug(ARTICLE.getSlug())).thenReturn(Optional.of(ARTICLE));

        assertThatThrownBy(() -> articleService.createNewArticle(data, USER)).isInstanceOf(ArticleConflictException.class);
    }

    @Test
    @DisplayName("Should update the information of article")
    void updateArticleTestSuccess() {
        RequestUpdateArticleDto data = new RequestUpdateArticleDto("new title", "newDescription",
                "newBody");

        when(articleRepository.findBySlug(ARTICLE.getSlug())).thenReturn(Optional.of(ARTICLE));
        when(articleRepository.save(ARTICLE)).thenReturn(ARTICLE);

        Article sut = articleService.updateArticle(USER, ARTICLE.getSlug(), data);

        assertThat(sut.getSlug()).isEqualTo("new-title");
        assertThat(sut.getDescription()).isEqualTo("newDescription");
        assertThat(sut.getBody()).isEqualTo("newBody");
    }

    @Test
    @DisplayName("Should remove and article from DB")
    void removeArticleTestSuccess() {
        String slug = "slug";
        when(articleRepository.findBySlug(slug)).thenReturn(Optional.of(ARTICLE));

        articleService.removeArticle(slug);

        verify(articleRepository).deleteBySlug(slug);
    }

    @Test
    @DisplayName("Should throws exception because article not exists in BD")
    void removeArticleTestFailed() {
        String slug = "slug";

        when(articleRepository.findBySlug(slug)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> articleService.removeArticle(slug)).isInstanceOf(ArticleNotFoundException.class);
    }
}