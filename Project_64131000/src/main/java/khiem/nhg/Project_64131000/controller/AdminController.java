package khiem.nhg.Project_64131000.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.model.ArticleTag;
import khiem.nhg.Project_64131000.model.User;
import khiem.nhg.Project_64131000.service.ArticleService;
import khiem.nhg.Project_64131000.service.ArticleTagService;
import khiem.nhg.Project_64131000.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@GetMapping("/dashboard")
	public String adminDashboard(Model model) {
	    model.addAttribute("title", "Admin Dashboard");
	    model.addAttribute("content", "frontEndModel/admin/dashboard :: content");
	    return "frontEndModel/admin_layout";
	}
    @Autowired
    private ArticleService articleService;

    @GetMapping("/articles")
    public String listArticles(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "frontEndModel/admin/article/list";
    }

    @GetMapping("/articles/new")
    public String newArticleForm(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("users", userService.getAllUsers());
        return "frontEndModel/admin/article/form";
    }
    
    @Autowired
    private ArticleTagService articleTagService;

    @PostMapping("/articles/save")
    public String saveArticle(@ModelAttribute Article article, 
                              @RequestParam("publishedAt") String publishedAtStr,
                              @RequestParam("authorId") Long authorId,
                              @RequestParam(name="tags", required = false) String tagsStr,
                              Model model) {
        try {
            if (publishedAtStr != null && !publishedAtStr.isEmpty()) {
                LocalDate publishedAt = LocalDate.parse(publishedAtStr, DateTimeFormatter.ISO_DATE);
                article.setPublishedAt(publishedAt.atStartOfDay());
            }
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Ngày đăng không hợp lệ.");
            model.addAttribute("article", article);
            return "frontEndModel/admin/article/form";
        }
        User author = userService.getUserById(authorId).orElse(null);
        if (author == null) {
            model.addAttribute("error", "Tác giả không hợp lệ.");
            model.addAttribute("article", article);
            model.addAttribute("users", userService.getAllUsers());
            return "frontEndModel/admin/article/form";
        }
        article.setAuthor(author);
        article = articleService.save(article);
        List<ArticleTag> tags = new ArrayList<>();

        if (tagsStr != null && !tagsStr.isEmpty()) {
            String[] tagNames = tagsStr.split(",");
            for (String rawTagName : tagNames) {
                String tagName = rawTagName.trim();
                ArticleTag tag = articleTagService.createTagIfNotExists(article, tagName);
                tags.add(tag);
            }
        }
        article.setTags(tags);
        articleService.save(article);

        return "redirect:/admin/articles";
    }


    @GetMapping("/articles/edit/{id}")
    public String editArticle(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        if (article == null) return "redirect:/admin/articles";
        model.addAttribute("article", article);
        String tagsString = "";
        if (article.getTags() != null && !article.getTags().isEmpty()) {
            tagsString = article.getTags().stream()
                              .map(ArticleTag::getTags)
                              .collect(Collectors.joining(", "));
        }
        model.addAttribute("tagsString", tagsString);

        model.addAttribute("users", userService.getAllUsers());
        return "frontEndModel/admin/article/form";
    }

    @GetMapping("/articles/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.deleteById(id);
        return "redirect:/admin/articles";
    }
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "frontEndModel/admin/user/list";
    }

    @GetMapping("/users/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return "frontEndModel/admin/user/form";
    }

    @GetMapping("/admin/user/form")
    public String showUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "frontEndModel/admin/user/form";
    }
    
    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
    	Optional<User> optionalUser = userService.getUserById(id);
    	User user = optionalUser.orElse(null);
        if (user == null) return "redirect:/admin/users";
        model.addAttribute("user", user);
        return "frontEndModel/admin/user/form";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
    	userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}