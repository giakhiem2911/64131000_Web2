package khiem.nhg.Project_64131000.controller;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Comparator;

@Controller
public class HomeController {

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Article> featuredArticles = articleRepository.findFeaturedArticles();
        Article latestArticle = featuredArticles.stream()
                .filter(article -> article.getPublishedAt() != null)
                .max(Comparator.comparing(Article::getPublishedAt))
                .orElse(null);
        if (latestArticle != null) {
            featuredArticles.remove(latestArticle);
        }

        List<Article> exploreArticles = articleRepository.findByCategory("Khám phá");
        List<Article> productArticles = articleRepository.findByCategory("Sản phẩm công nghệ");
        List<Article> latestArticles = articleRepository.findTop5ByOrderByPublishedAtDesc();
        List<Article> articles = articleRepository.findAll(); 
        
        model.addAttribute("articles", articles);
        model.addAttribute("latestArticle", latestArticle);
        model.addAttribute("exploreArticles", exploreArticles);
        model.addAttribute("productArticles", productArticles);
        model.addAttribute("latestArticles", latestArticles);

        return "frontEndModel/index";
    }



    @GetMapping("/search")
    public String searchArticles(@RequestParam("keyword") String keyword, Model model) {
        List<Article> results = articleRepository.searchByKeyword(keyword); // Bạn cần triển khai hàm này
        model.addAttribute("articles", results);
        model.addAttribute("keyword", keyword);
        return "frontEndModel/searchResults";
    }

}