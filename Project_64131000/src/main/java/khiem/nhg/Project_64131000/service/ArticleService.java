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
    public List<Article> getArticlesByCategory(String category) {
        return articleRepository.findByCategoryIgnoreCase(category);
    }
    public List<Article> getFeaturedArticles() {
        return articleRepository.findFeaturedArticles();
    }
    public List<Article> searchByKeyword(String keyword) {
        return articleRepository.searchByKeyword(keyword);
    }
    public Article findById(Long articleId) {
        return articleRepository.findById(articleId).orElse(null);
    }

    public List<Article> findTop5Latest() {
        return articleRepository.findTop5ByOrderByPublishedAtDesc();
    }
    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

}