package khiem.nhg.Project_64131000.controller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.model.ArticleTag;
import khiem.nhg.Project_64131000.model.User;
import khiem.nhg.Project_64131000.service.*;

import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired private ArticleService articleService;
    @Autowired private ArticleTagService articleTagService;
    @Autowired private UserService userService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("title", "Admin Dashboard");
        model.addAttribute("content", "frontEndModel/admin/dashboard :: content");
        return "frontEndModel/admin_layout";
    }

    @GetMapping("/articles")
    public String listArticles(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "frontEndModel/admin/article/list";
    }

    @GetMapping("/articles/new")
    public String newArticleForm(Model model) {
        Article article = new Article();
        article.setAuthor(new User());
        model.addAttribute("article", article);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("tags", articleTagService.getAllTags());
        return "frontEndModel/admin/article/form";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                    return;
                }
                DateTimeFormatter formatterWithFraction = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .optionalStart()
                    .appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true)
                    .optionalEnd()
                    .toFormatter();

                DateTimeFormatter formatterWithoutSeconds = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

                LocalDateTime dateTime = null;
                try {
                    dateTime = LocalDateTime.parse(text, formatterWithFraction);
                } catch (Exception e1) {
                    try {
                        dateTime = LocalDateTime.parse(text, formatterWithoutSeconds);
                    } catch (Exception e2) {
                        throw new IllegalArgumentException("Không thể parse ngày giờ: " + text);
                    }
                }
                setValue(dateTime);
            }
        });
    }



    @PostMapping("/articles/save")
    public String saveArticle(
        @ModelAttribute("article") @Validated Article article,
        BindingResult result,
        @RequestParam(name = "tags", required = false) List<String> tagNames,
        @RequestParam(name = "image", required = false) MultipartFile imageFile,
        Model model) {

        if (article.getUpdatedAt() == null) {
            article.setUpdatedAt(LocalDateTime.now());
        }

        if (result.hasErrors()) {
            logger.error("Validation errors: {}", result.getFieldErrors());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("tags", articleTagService.getAllTags());
            model.addAttribute("tagsString", tagNames != null ? String.join(",", tagNames) : "");
            return "frontEndModel/admin/article/form";
        }

        if (article.getAuthor() == null || article.getAuthor().getUserId() == null) {
            result.rejectValue("author.userId", "NotNull", "Vui lòng chọn tác giả.");
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("tags", articleTagService.getAllTags());
            return "frontEndModel/admin/article/form";
        }

        Optional<User> authorOpt = userService.getUserById(article.getAuthor().getUserId());
        if (authorOpt.isEmpty()) {
            result.rejectValue("author.userId", "NotFound", "Tác giả không tồn tại.");
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("tags", articleTagService.getAllTags());
            return "frontEndModel/admin/article/form";
        }
        article.setAuthor(authorOpt.get());

        List<ArticleTag> tags = new ArrayList<>();
        if (tagNames != null) {
            for (String tagName : tagNames) {
                if (!tagName.trim().isEmpty()) {
                    tags.add(articleTagService.createTagIfNotExists(article, tagName.trim()));
                }
            }
        }
        article.setTags(tags);

        if ("published".equals(article.getStatus()) && article.getPublishedAt() == null) {
            article.setPublishedAt(LocalDateTime.now());
        }

        // ✅ Xử lý ảnh
        if (imageFile != null && !imageFile.isEmpty()) {
            String originalName = imageFile.getOriginalFilename();
            String extension = originalName != null && originalName.contains(".")
                    ? originalName.substring(originalName.lastIndexOf('.'))
                    : "";

            String filename = UUID.randomUUID() + extension;

         // Đường dẫn lưu ảnh - ngoài thư mục src
            String uploadDir = System.getProperty("user.dir") + "/uploads/images";
            Path uploadPath = Paths.get(uploadDir);

            try {
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(filename);
                imageFile.transferTo(filePath.toFile());

                article.setImageUrl("/uploads/images/" + filename);
            } catch (IOException e) {
                logger.error("Lỗi khi lưu ảnh: {}", e.getMessage());
            }

        }

        try {
            articleService.save(article);
        } catch (DataIntegrityViolationException e) {
            logger.error("Lỗi lưu Article: {}", e.getMessage());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("tags", articleTagService.getAllTags());
            return "frontEndModel/admin/article/form";
        }

        return "redirect:/admin/articles";
    }


    @GetMapping("/articles/edit/{id}")
    public String editArticle(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        if (article == null) return "redirect:/admin/articles";

        model.addAttribute("article", article);
        model.addAttribute("tags", articleTagService.getAllTags());
        model.addAttribute("users", userService.getAllUsers());
        String tagsString = article.getTags() != null ? article.getTags().stream()
            .map(ArticleTag::getTags)
            .collect(Collectors.joining(", ")) : "";
        model.addAttribute("tagsString", tagsString);
        return "frontEndModel/admin/article/form";
    }

    @GetMapping("/articles/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.deleteById(id);
        return "redirect:/admin/articles";
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
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
    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user, BindingResult result,Model model) {
        if (result.hasErrors()) {
            return "frontEndModel/admin/user/form";
        }
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
