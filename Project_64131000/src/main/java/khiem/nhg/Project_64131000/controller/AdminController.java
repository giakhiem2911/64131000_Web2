package khiem.nhg.Project_64131000.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("title", "Admin Dashboard");
        return "frontEndModel/admin/dashboard";
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
                             @RequestParam(value = "publishedAt", required = false) String publishedAtStr,
                             @RequestParam("authorId") Long authorId,
                             @RequestParam(name = "tags", required = false) String tagIds,
                             Model model) {
        logger.debug("Saving article: articleId={}, title={}, authorId={}, publishedAtStr={}, tags={}", 
                     article.getArticleId(), article.getTitle(), authorId, publishedAtStr, tagIds);

        // Xử lý ngày đăng
        if (publishedAtStr != null && !publishedAtStr.isEmpty()) {
            try {
                LocalDate publishedAt = LocalDate.parse(publishedAtStr, DateTimeFormatter.ISO_LOCAL_DATE);
                article.setPublishedAt(publishedAt.atStartOfDay());
            } catch (DateTimeParseException e) {
                logger.error("Invalid publishedAt: {}", publishedAtStr, e);
                model.addAttribute("error", "Ngày đăng không hợp lệ. Vui lòng nhập định dạng yyyy-MM-dd.");
                model.addAttribute("article", article);
                model.addAttribute("users", userService.getAllUsers());
                model.addAttribute("tagsString", tagIds);
                return "frontEndModel/admin/article/form";
            }
        } else {
            article.setPublishedAt(null);
        }

        // Kiểm tra tác giả
        User author = userService.getUserById(authorId).orElse(null);
        if (author == null) {
            logger.error("Invalid authorId: {}", authorId);
            model.addAttribute("error", "Tác giả không hợp lệ.");
            model.addAttribute("article", article);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("tagsString", tagIds);
            return "frontEndModel/admin/article/form";
        }
        article.setAuthor(author);

        // Kiểm tra nội dung
        if (article.getContent() == null || article.getContent().trim().isEmpty()) {
            logger.error("Content is empty for articleId: {}", article.getArticleId());
            model.addAttribute("error", "Nội dung bài viết không được để trống.");
            model.addAttribute("article", article);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("tagsString", tagIds);
            return "frontEndModel/admin/article/form";
        }

        // Xử lý bài viết
        if (article.getArticleId() != null) {
            // Cập nhật bài viết
            Article existingArticle = articleService.findById(article.getArticleId());
            if (existingArticle == null) {
                logger.error("Article not found: articleId={}", article.getArticleId());
                model.addAttribute("error", "Bài viết không tồn tại.");
                model.addAttribute("article", article);
                model.addAttribute("users", userService.getAllUsers());
                model.addAttribute("tagsString", tagIds);
                return "frontEndModel/admin/article/form";
            }
            existingArticle.setTitle(article.getTitle());
            existingArticle.setContent(article.getContent());
            existingArticle.setPublishedAt(article.getPublishedAt());
            existingArticle.setAuthor(author);
            existingArticle.setImages(article.getImages());
            existingArticle.setStatus(article.getStatus());

            // Xử lý tags
            if (tagIds != null && !tagIds.isEmpty()) {
                List<ArticleTag> tags = articleTagService.findByTag(tagIds);
                existingArticle.setTags(tags);
            } else {
                existingArticle.setTags(null);
            }

            articleService.save(existingArticle);
            logger.debug("Updated article: articleId={}", existingArticle.getArticleId());
        } else {
            // Tạo mới bài viết
            if (tagIds != null && !tagIds.isEmpty()) {
                List<ArticleTag> tags = articleTagService.findByTag(tagIds);
                article.setTags(tags);
            } else {
                article.setTags(null);
            }
            articleService.save(article);
            logger.debug("Created new article: articleId={}", article.getArticleId());
        }

        return "redirect:/admin/articles";
    }

    @GetMapping("/articles/edit/{id}")
    public String editArticle(@PathVariable Long id, Model model) {
        logger.debug("Editing article: id={}", id);
        Article article = articleService.findById(id);
        if (article == null) {
            logger.error("Article not found: id={}", id);
            return "redirect:/admin/articles";
        }
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

    @GetMapping("/user/form")
    public String showUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "frontEndModel/admin/user/form";
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        if (user.getUserId() == null) {
            user.setCreatedAt(LocalDateTime.now());
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        } else {
            Optional<User> existingUserOpt = userService.getUserById(user.getUserId());
            if (existingUserOpt.isPresent()) {
                User existingUser = existingUserOpt.get();
                if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
                    user.setPasswordHash(existingUser.getPasswordHash());
                } else {
                    user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
                }
            }
        }

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setUpdatedAt(LocalDateTime.now());
        userService.save(user);
        return "redirect:/admin/users";
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