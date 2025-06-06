package khiem.nhg.Project_64131000.controller;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.model.User;
import khiem.nhg.Project_64131000.model.UserActivityDTO;
import khiem.nhg.Project_64131000.repository.ArticleRepository;
import khiem.nhg.Project_64131000.repository.UserRepository;
import khiem.nhg.Project_64131000.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.annotation.PostConstruct;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.jsoup.Jsoup;


@Controller
public class HomeController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct // Chạy khi ứng dụng khởi động
    public void encodePasswords() {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            String password = user.getPasswordHash();
            // Kiểm tra xem có phải bcrypt hay không
            if (!password.startsWith("$2a$")) {
                String encoded = passwordEncoder.encode(password);
                user.setPasswordHash(encoded);
                userService.save(user);
            }
        }
    }

    @GetMapping("/")
    public String index(Model model) {
        // Add currentUser to model for header display
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
            List<UserActivityDTO> activities = userService.getRecentActivities(currentUser);
            model.addAttribute("userActivities", activities);
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
            latestArticle.setContent(stripHtml(latestArticle.getContent()));
        }

        List<Article> exploreArticles = articleRepository.findByCategory("Khám phá");
        List<Article> productArticles = articleRepository.findByCategory("Sản phẩm công nghệ");
        List<Article> latestArticles = articleRepository.findTop5ByOrderByPublishedAtDesc();
        List<Article> articles = articleRepository.findAll();
        for (Article a : articles) {
            a.setContent(stripHtml(a.getContent()));
        }
        for (Article a : exploreArticles) {
            a.setContent(stripHtml(a.getContent()));
        }
        for (Article a : productArticles) {
            a.setContent(stripHtml(a.getContent()));
        }
        for (Article a : latestArticles) {
            a.setContent(stripHtml(a.getContent()));
        }
        model.addAttribute("articles", articles);
        model.addAttribute("latestArticle", latestArticle);
        model.addAttribute("exploreArticles", exploreArticles);
        model.addAttribute("productArticles", productArticles);
        model.addAttribute("latestArticles", latestArticles);

        return "frontEndModel/index";
    }
    private String stripHtml(String html) {
        if (html == null) return "";
        return Jsoup.parse(html).text();
    }
    
    @GetMapping("/category/kham-pha")
    public String khamPhaCategory(Model model) {
        // Article-related logic
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
            model.addAttribute("currentUser", currentUser);
        }
        List<Article> featuredArticles = articleRepository.findFeaturedArticles();
        Article latestArticle = featuredArticles.stream()
                .filter(article -> article.getPublishedAt() != null)
                .max(Comparator.comparing(Article::getPublishedAt))
                .orElse(null);
        if (latestArticle != null) {
            featuredArticles.remove(latestArticle);
            latestArticle.setContent(stripHtml(latestArticle.getContent()));
        }
        List<Article> exploreArticles = articleRepository.findByCategory("Khám phá");
        List<Article> latestArticles = articleRepository.findTop5ByOrderByPublishedAtDesc();
        List<Article> articles = articleRepository.findAll();
        for (Article a : articles) {
            a.setContent(stripHtml(a.getContent()));
        }
        for (Article a : exploreArticles) {
            a.setContent(stripHtml(a.getContent()));
        }
        for (Article a : latestArticles) {
            a.setContent(stripHtml(a.getContent()));
        }
        model.addAttribute("articles", articles);
        model.addAttribute("latestArticle", latestArticle);
        model.addAttribute("exploreArticles", exploreArticles);
        model.addAttribute("latestArticles", latestArticles);
        return "frontEndModel/category/khamPha"; 
    }
    @GetMapping("/category/san-pham-cong-nghe")
    public String sanPhamCongNgheCategory(Model model) {
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
            latestArticle.setContent(stripHtml(latestArticle.getContent()));
        }
        List<Article> productArticles = articleRepository.findByCategory("Sản phẩm công nghệ");
        List<Article> latestArticles = articleRepository.findTop5ByOrderByPublishedAtDesc();
        List<Article> articles = articleRepository.findAll();
        for (Article a : articles) {
            a.setContent(stripHtml(a.getContent()));
        }
        for (Article a : productArticles) {
            a.setContent(stripHtml(a.getContent()));
        }
        for (Article a : latestArticles) {
            a.setContent(stripHtml(a.getContent()));
        }
        model.addAttribute("articles", articles);
        model.addAttribute("latestArticle", latestArticle);
        model.addAttribute("productArticles", productArticles);
        model.addAttribute("latestArticles", latestArticles);
        return "frontEndModel/category/sanPhamCongNghe";
    }
}