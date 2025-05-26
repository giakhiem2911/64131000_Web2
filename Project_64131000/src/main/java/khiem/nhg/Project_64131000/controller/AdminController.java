package khiem.nhg.Project_64131000.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.model.ArticleImage;
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
        model.addAttribute("tags", articleTagService.getAllTags()); 
        return "frontEndModel/admin/article/form";
    }

    @Autowired
    private ArticleTagService articleTagService;

    @PostMapping("/articles/save")
    public String saveArticle(@Validated @ModelAttribute("article") Article article, BindingResult result,
                              @RequestParam(name = "tags", required = false) List<String> tagNames,
                              @RequestParam(name = "images", required = false) MultipartFile[] images,
                              Model model) {
    	
        if (article.getAuthor() == null || article.getAuthor().getUserId() == null) {
            model.addAttribute("error", "Vui lòng chọn tác giả.");
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("tags", articleTagService.getAllTags());
            return "frontEndModel/admin/article/form";
        }

        List<ArticleTag> tags = new ArrayList<>();
        if (tagNames != null) {
            for (String tagName : tagNames) {
                ArticleTag tag = articleTagService.createTagIfNotExists(article, tagName.trim());
                tags.add(tag);
            }
        }
        article.setTags(tags);

        article.setUpdatedAt(LocalDateTime.now());
        articleService.save(article);
        
     // Xử lý ảnh
        if (images != null) {
            List<ArticleImage> imageList = new ArrayList<>();
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    try {
                        String uploadDir = "src/main/resources/static/images/";
                        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        File dest = new File(uploadDir + fileName);
                        file.transferTo(dest);

                        // Tạo đối tượng ArticleImage
                        ArticleImage articleImage = new ArticleImage();
                        articleImage.setArticle(article);
                        articleImage.setImageUrl("/images/" + fileName);
                        imageList.add(articleImage);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Gán vào article và lưu lại
            article.setImages(imageList);
            articleService.save(article);
        }

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
        model.addAttribute("tags", articleTagService.getAllTags());
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