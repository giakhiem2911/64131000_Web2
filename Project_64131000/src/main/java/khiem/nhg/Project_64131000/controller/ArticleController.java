package khiem.nhg.Project_64131000.controller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;

import org.jsoup.Jsoup;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.model.ArticleTag;
import khiem.nhg.Project_64131000.model.Comment;
import khiem.nhg.Project_64131000.model.Interaction;
import khiem.nhg.Project_64131000.model.User;
import khiem.nhg.Project_64131000.repository.ArticleRepository;
import khiem.nhg.Project_64131000.repository.UserRepository;
import khiem.nhg.Project_64131000.service.ArticleService;
import khiem.nhg.Project_64131000.service.ArticleTagService;
import khiem.nhg.Project_64131000.service.CommentService;
import khiem.nhg.Project_64131000.service.InteractionService;
import khiem.nhg.Project_64131000.service.UserService;

@Controller
public class ArticleController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired private ArticleService articleService;
    @Autowired private ArticleRepository articleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private ArticleTagService articleTagService;
    @Autowired private CommentService commentService;

    @Autowired
    private InteractionService interactionService;
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
        // Ngăn binding tự động cho tags
        binder.setDisallowedFields("tags");
    }
    @PostMapping("/articles/{id}/like")
    public ResponseEntity<String> likeArticle(@PathVariable("id") Long articleId, Principal principal) {
        // principal là user đăng nhập
        String userEmail = principal.getName();

        interactionService.likeArticle(articleId, userEmail);

        return ResponseEntity.ok("Liked");
    }
    
    @PostMapping("/articles/{id}/unlike")
    public ResponseEntity<String> unlikeArticle(@PathVariable("id") Long articleId, Principal principal) {
        interactionService.unlikeArticle(articleId, principal.getName());
        return ResponseEntity.ok("Unliked");
    }

    @PostMapping("/articles/{id}/comment/ajax")
    @ResponseBody
    public ResponseEntity<?> postCommentAjax(@PathVariable("id") Long id,
                                             @RequestParam("content") String content,
                                             Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            User user = userService.findByEmail(email).orElse(null);
            Article article = articleService.findById(id);

            if (user != null && article != null) {
                Comment comment = new Comment();
                comment.setContent(content);
                comment.setCreatedAt(LocalDateTime.now());
                comment.setUser(user);
                comment.setArticle(article);

                commentService.saveComment(comment);

                // Trả về dữ liệu JSON của comment mới
                Map<String, Object> response = new HashMap<>();
                response.put("fullName", user.getFullName());
                response.put("createdAt", comment.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                response.put("content", comment.getContent());

                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập.");
    }



    @GetMapping("/articles/new")
    public String showCreateForm(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("tags", "");
        return "frontEndModel/articleForm";
    }

    @PostMapping("/articles")
    public String saveArticle(
            @Valid @ModelAttribute("article") Article article,
            BindingResult result,
            @RequestParam(value = "tags", required = false) String tagsString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            Model model,
            Principal principal)
    {
        logger.info("Bắt đầu xử lý POST /articles, principal: {}", principal != null ? principal.getName() : "null");

        // Kiểm tra người dùng đăng nhập
        if (principal == null) {
            logger.warn("Principal is null, redirecting to login");
            return "redirect:/login?error=true";
        }

        User currentUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if (currentUser == null) {
            logger.error("User not found for email: {}", principal.getName());
            model.addAttribute("error", "Không tìm thấy người dùng. Vui lòng đăng nhập lại.");
            model.addAttribute("tagsString", tagsString);
            return "frontEndModel/articleForm";
        }
        article.setAuthor(currentUser);

        // Gán updatedAt tự động
        article.setUpdatedAt(LocalDateTime.now());

        // Gán publishedAt nếu trạng thái là published
        if ("published".equals(article.getStatus()) && article.getPublishedAt() == null) {
            article.setPublishedAt(LocalDateTime.now());
        }

        // Xử lý ảnh
        if (imageFile != null && !imageFile.isEmpty()) {
            String originalName = imageFile.getOriginalFilename();
            if (originalName != null && !originalName.matches(".*\\.(jpg|jpeg|png|gif)$")) {
                model.addAttribute("error", "Chỉ hỗ trợ các định dạng ảnh: jpg, jpeg, png, gif");
                model.addAttribute("tagsString", tagsString);
                return "frontEndModel/articleForm";
            }

            String extension = originalName != null && originalName.contains(".")
                    ? originalName.substring(originalName.lastIndexOf('.'))
                    : "";
            String filename = UUID.randomUUID() + extension;
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
                logger.error("Lỗi khi lưu ảnh: {}", e.getMessage(), e);
                model.addAttribute("error", "Không thể tải ảnh lên: " + e.getMessage());
                model.addAttribute("tagsString", tagsString);
                return "frontEndModel/articleForm";
            }
        }

        // Kiểm tra lỗi validation
        if (result.hasErrors()) {
            logger.error("Validation errors: {}", result.getFieldErrors());
            model.addAttribute("tagsString", tagsString);
            return "frontEndModel/articleForm";
        }

        try {
        	article.getTags().clear();
            logger.info("Saving article: title={}, content={}, category={}, status={}, author={}",
                        article.getTitle(), article.getContent(), article.getCategory(),
                        article.getStatus(), article.getAuthor().getUserId());
            articleService.save(article);

            List<ArticleTag> tags = article.getTags();
            if (tagsString != null && !tagsString.trim().isEmpty()) {
                String[] tagNames = tagsString.split(",");
                for (String tagName : tagNames) {
                    String trimmedTag = tagName.trim();
                    if (!trimmedTag.isEmpty()) {
                        ArticleTag tag = articleTagService.createTagIfNotExists(article, trimmedTag);
                        if (tag != null) {
                            tags.add(tag);
                        } else {
                            logger.warn("Không thể tạo tag: {}", trimmedTag);
                        }
                    }
                }
                articleService.save(article);
            }

            logger.info("Article saved successfully, redirecting to /articles/{}", article.getArticleId());
            return "redirect:/articles/" + article.getArticleId();

        } catch (DataIntegrityViolationException e) {
            logger.error("Lỗi khi lưu article: {}", e.getMessage(), e);
            model.addAttribute("error", "Dữ liệu không hợp lệ hoặc bị trùng lặp: " + e.getMessage());
            model.addAttribute("tagsString", tagsString);
            return "frontEndModel/articleForm";
        } catch (Exception e) {
            logger.error("Lỗi không xác định khi lưu article: {}", e.getMessage(), e);
            model.addAttribute("error", "Có lỗi xảy ra khi lưu bài viết: " + e.getMessage());
            model.addAttribute("tagsString", tagsString);
            return "frontEndModel/articleForm";
        }
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
        List<Interaction> interactions = interactionService.findAllByArticle(article);
        article.setInteractions(interactions);
        model.addAttribute("article", article);
        model.addAttribute("latestArticles", articleService.findTop5Latest());
        

        List<Comment> comments = commentService.getCommentsByArticleId(articleId);
        model.addAttribute("comments", comments);
        
        return "/frontEndModel/articleDetail";
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
}