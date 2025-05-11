package khiem.nhg.Project_64131000.service;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> getArticlesByAuthorId(Long userId) {
        return articleRepository.findByAuthorUserId(userId);
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article updateArticle(Long id, Article articleDetails) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            article.setTitle(articleDetails.getTitle());
            article.setContent(articleDetails.getContent());
            article.setCategory(articleDetails.getCategory());
            article.setAuthor(articleDetails.getAuthor());
            article.setStatus(articleDetails.getStatus());
            article.setViews(articleDetails.getViews());
            article.setPublishedAt(articleDetails.getPublishedAt());
            article.setUpdatedAt(articleDetails.getUpdatedAt());
            return articleRepository.save(article);
        }
        return null;
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}