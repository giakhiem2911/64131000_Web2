package khiem.nhg.Project_64131000.controller;

import java.util.Comparator;
import java.util.List;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.model.User;
import khiem.nhg.Project_64131000.repository.ArticleRepository;
import khiem.nhg.Project_64131000.repository.UserRepository;
import khiem.nhg.Project_64131000.service.ArticleService;
import khiem.nhg.Project_64131000.service.ArticleTagService;
import khiem.nhg.Project_64131000.service.UserService;

@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    
    @Autowired
    private ArticleTagService articleTagService;
    
    @GetMapping("/articles/new")
    public String showCreateForm(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("tags", articleTagService.getAllTags());
        return "/frontEndModel/articleForm";
    }
    @PostMapping("/articles")
    public String createArticle(@Valid Article article, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/frontEndModel/articleForm";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";  // Chuyển đến trang đăng nhập nếu chưa đăng nhập
        }

        User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }

        article.setAuthor(currentUser);
        articleService.save(article);

        return "redirect:/articles/" + article.getId();
    }
    private String stripHtml(String html) {
        if (html == null) return "";
        return Jsoup.parse(html).text();
    }
    @GetMapping("/search")
    public String searchArticles(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Article> featuredArticles = articleRepository.findFeaturedArticles();
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("results", articleService.searchByKeyword(keyword));
        } else {
            model.addAttribute("results", List.of());
        }
        Article latestArticle = featuredArticles.stream()
                .filter(article -> article.getPublishedAt() != null)
                .max(Comparator.comparing(Article::getPublishedAt))
                .orElse(null);
        if (latestArticle != null) {
            featuredArticles.remove(latestArticle);
            latestArticle.setContent(stripHtml(latestArticle.getContent()));
        }
        List<Article> latestArticles = articleRepository.findTop5ByOrderByPublishedAtDesc();
        for (Article a : latestArticles) {
            a.setContent(stripHtml(a.getContent()));
        }
        model.addAttribute("keyword", keyword);
        model.addAttribute("latestArticles", latestArticle);

        return "/frontEndModel/search";
    }
    
    @GetMapping("/articles/{articleId}")
    public String getArticleDetail(@PathVariable Long articleId, Model model) {
        Article article = articleService.findById(articleId);
        if (article == null) {
            return "redirect:/";
        }
        // Add currentUser to model for header display
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
            model.addAttribute("currentUser", currentUser);
        }
        model.addAttribute("article", article);

        model.addAttribute("latestArticles", articleService.findTop5Latest());
        
        return "/frontEndModel/articleDetail"; 
    }

}
