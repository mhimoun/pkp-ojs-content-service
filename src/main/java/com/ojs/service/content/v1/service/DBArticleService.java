package com.ojs.service.content.v1.service;

import com.ojs.service.content.v1.domain.ArticleRepository;
import com.ojs.service.content.v1.domain.Submissions;
import com.ojs.service.content.v1.dto.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DBArticleService implements ArticleService {

    @Autowired
    private final ArticleRepository articleRepository;

    public DBArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> getPublishedArticles() {

        List<Article> articles = new ArrayList<>();
        List<Submissions> publishedSubmissions = articleRepository.findByStatus(ArticleRepository.ARTICLE_STATUS_PUBLISHED);

        for (Submissions submissions : publishedSubmissions) {
            articles.add(populateArticleFromSubmission(submissions));
        }
        return articles;
    }

    @Override
    public Article getArticleById(long articleId) {
        return populateArticleFromSubmission(articleRepository.findBySubmissionIdAndStatus(articleId, ArticleRepository.ARTICLE_STATUS_PUBLISHED));
    }

    private Article populateArticleFromSubmission(Submissions submissions) {
        Article article = new Article(submissions.getSubmissionId(), submissions.getPublishedSubmission().getIssueId(), 0);
        article.setDatePublished(submissions.getPublishedSubmission().getDatePublished());

        return article;
    }
}