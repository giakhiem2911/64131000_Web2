package khiem.nhg.Project_64131000.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.model.User;
import khiem.nhg.Project_64131000.repository.ArticleRepository;
import khiem.nhg.Project_64131000.repository.UserRepository;
import khiem.nhg.Project_64131000.service.ArticleService;

@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/search")
    public String searchArticles(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Article> latestArticles = articleRepository.findTop5ByOrderByPublishedAtDesc();

        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("results", articleService.searchByKeyword(keyword));
        } else {
            model.addAttribute("results", List.of());
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("latestArticles", latestArticles);

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
