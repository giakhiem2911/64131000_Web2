package khiem.nhg.Project_64131000.controller;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Comparator;

@Controller
public class HomeController {

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Article> articles = articleRepository.findFeaturedArticles();

        // Tìm bài viết mới nhất dựa trên publishedAt
        Article latestArticle = articles.stream()
                .filter(article -> article.getPublishedAt() != null)
                .max(Comparator.comparing(Article::getPublishedAt))
                .orElse(null);

        if (latestArticle != null) {
            articles.remove(latestArticle);
        }

        model.addAttribute("latestArticle", latestArticle);
        model.addAttribute("articles", articles);
        return "frontEndModel/index";
    }
}