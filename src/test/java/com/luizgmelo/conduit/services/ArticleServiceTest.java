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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;
import java.util.List;

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
    private TagRepository tagRepository;

    @Test
    @DisplayName("Should return a List of two article filtered by tag from DB")
    void listArticles_ByTag_Success() {
        Article secondArticle = new Article("title2", "title2", "description2", "body2", Set.of(new Tag("tag")), USER);
        Article failedArticle = new Article("title3", "title3", "description3", "body3", Set.of(new Tag("anothertag")), USER);

        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        PageImpl<Article> page = new PageImpl<>(List.of(ARTICLE, secondArticle));

        when(articleRepository.findByTag("tag", pageable)).thenReturn(page);

        List<Article> sut = articleService.listArticles("tag", null, null, 5, 0);

        assertThat(sut).isNotNull();
        assertThat(sut.size()).isEqualTo(2);
        assertThat(sut.getFirst().getSlug()).isEqualTo(ARTICLE.getSlug());
        assertThat(sut.getLast().getSlug()).isEqualTo(secondArticle.getSlug());
        assertThat(sut).isNotNull();
        assertThat(sut.contains(failedArticle)).isFalse();
    }

    @Test
    @DisplayName("Should return a List of two article filtered by tag from DB")
    void listArticles_ByAuthor_Success() {
        Article secondArticle = new Article("title2", "title2", "description2", "body2", Set.of(new Tag("tag")), USER);
        Article failedArticle = new Article("title3", "title3", "description3", "body3", Set.of(new Tag("anothertag")), new User("teste", "teste@email.com", "testepassword"));

        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        PageImpl<Article> page = new PageImpl<>(List.of(ARTICLE, secondArticle));

        when(articleRepository.findByAuthor(USER.getUsername(), pageable)).thenReturn(page);

        List<Article> sut = articleService.listArticles(null, USER.getUsername(), null, 5, 0);

        assertThat(sut).isNotNull();
        assertThat(sut.size()).isEqualTo(2);
        assertThat(sut.getFirst().getSlug()).isEqualTo(ARTICLE.getSlug());
        assertThat(sut.getLast().getSlug()).isEqualTo(secondArticle.getSlug());
        assertThat(sut).isNotNull();
        assertThat(sut.contains(failedArticle)).isFalse();
    }

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

        ArticleNotFoundException thrown = Assertions.assertThrows(ArticleNotFoundException.class, () -> articleService.getArticle(slug));

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

    @Test
    @DisplayName("Should update the information of article")
    void updateArticleTestSuccess() {
        RequestArticleDTO data = new RequestArticleDTO("new title", "newDescription",
                "newBody", List.of("newTag"));

        when(articleRepository.save(ARTICLE)).thenReturn(ARTICLE);
        when(tagRepository.findByName("newTag")).thenReturn(Optional.of(new Tag("newTag")));

        articleService.updateArticle(ARTICLE, data);


        assertThat(ARTICLE.getSlug()).isEqualTo("new-title");
        assertThat(ARTICLE.getDescription()).isEqualTo("newDescription");
        assertThat(ARTICLE.getBody()).isEqualTo("newBody");
        assertThat(ARTICLE.getTagList().getFirst()).isEqualTo("newTag");
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