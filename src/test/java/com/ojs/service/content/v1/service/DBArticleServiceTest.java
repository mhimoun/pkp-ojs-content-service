package com.ojs.service.content.v1.service;


import com.ojs.service.content.v1.domain.ArticleRepository;
import com.ojs.service.content.v1.domain.SubmissionSettings;
import com.ojs.service.content.v1.domain.Submissions;
import com.ojs.service.content.v1.dto.Article;
import com.ojs.service.content.v1.exception.ArticleNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.ojs.service.content.v1.domain.ArticleRepository.ARTICLE_STATUS_PUBLISHED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DBArticleServiceTest {

    ArticleService articleService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private ArticleRepository articleRepository;

    @Before
    public void setup() {
        articleService = new DBArticleService(articleRepository);
        when(articleRepository.findByStatus(ARTICLE_STATUS_PUBLISHED)).thenReturn(submissions());
    }

    @Test
    public void getPublishedArticles_shouldReturnEmptyListWhenNoPublishedArticles() throws Exception {

        ArticleRepository repository = mock(ArticleRepository.class);
        articleService = new DBArticleService(repository);
        List<Article> articles = articleService.getPublishedArticles();

        assertThat(articles.size()).isEqualTo(0);
        verify(repository).findByStatus(ARTICLE_STATUS_PUBLISHED);

    }

    @Test
    public void getPublishedArticles_shouldReturnResultsWhenSubmissionsPublishedInDB() throws Exception {

        List<Article> articles = articleService.getPublishedArticles();
        assertThat(articles.size()).isEqualTo(2);
    }


    @Test
    public void getPublishedArticles_shouldReturnArticlesWithId() throws Exception {

        List<Article> articles = articleService.getPublishedArticles();
        assertThat(articles.get(0).getArticleId()).isEqualTo(11);
        assertThat(articles.get(1).getArticleId()).isEqualTo(13);
    }

    @Test
    public void getPublishedArticles_shouldReturnArticlesWithIssueId() throws Exception {

        List<Article> articles = articleService.getPublishedArticles();
        assertThat(articles.get(0).getIssueId()).isEqualTo(3);
    }

    @Test
    public void getPublishedArticles_shouldReturnArticlesWithMainInfo() throws Exception {

        List<Article> articles = articleService.getPublishedArticles();
        assertThat(articles.get(0).getPages()).isEqualTo("some pages");
        assertThat(articles.get(0).getTitle()).isEqualTo("some title");
    }


    @Test
    public void getPublishedArticles_shouldNotReturnArticlesAbstract() throws Exception {

        List<Article> articles = articleService.getPublishedArticles();
        assertThat(articles.get(0).getArticleAbstract()).isNull();
    }


    @Test
    public void getArticleById_shouldReturnArticle() throws Exception {

        when(articleRepository.findBySubmissionIdAndStatus(11, ARTICLE_STATUS_PUBLISHED)).thenReturn(getSubmission());
        Article article = articleService.getArticleById(11);
        assertThat(article.getArticleId()).isEqualTo(11);
    }


    @Test
    public void getArticleById_shouldReturnArticleSettings() throws Exception {

        when(articleRepository.findBySubmissionIdAndStatus(11, ARTICLE_STATUS_PUBLISHED)).thenReturn(getSubmission());
        Article article = articleService.getArticleById(11);
        assertThat(article.getTitle()).isEqualTo("some title");
        assertThat(article.getCopyrightHolder()).isEqualTo("some copyrightHolder");
    }

    @Test
    public void getArticleById_shouldThrowExceptionIfArticleNotFoundOrNotPublished() throws Exception {

        this.thrown.expect(ArticleNotFoundException.class);

        articleService.getArticleById(404);

    }

    private List<Submissions> submissions() {
        return Arrays.asList(getSubmission(), new Submissions(13));
    }

    private Submissions getSubmission() {
        Submissions submissions = new Submissions(11);
        submissions.setPages("some pages");
        submissions.getPublishedSubmission().setIssueId(3);

        SubmissionSettings title = new SubmissionSettings(11, "en", "title", "some title", "string");
        SubmissionSettings copyrightHolder = new SubmissionSettings(11, "", "copyrightHolder", "some copyrightHolder", "string");

        List<SubmissionSettings> settings = Arrays.asList(title,copyrightHolder);
        submissions.setSubmissionSettings(settings);


        return submissions;
    }


}
