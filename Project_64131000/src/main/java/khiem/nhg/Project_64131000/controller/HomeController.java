package khiem.nhg.Project_64131000.controller;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.model.User;
import khiem.nhg.Project_64131000.repository.ArticleRepository;
import khiem.nhg.Project_64131000.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index(Model model) {
        // Add currentUser to model for header display
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
            model.addAttribute("currentUser", currentUser);
        }

        // Article-related logic
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
}