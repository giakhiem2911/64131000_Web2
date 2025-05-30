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
import khiem.nhg.Project_64131000.model.User;
import khiem.nhg.Project_64131000.repository.ArticleRepository;
import khiem.nhg.Project_64131000.repository.UserRepository;
import khiem.nhg.Project_64131000.service.ArticleService;
import khiem.nhg.Project_64131000.service.ArticleTagService;
import khiem.nhg.Project_64131000.service.UserService;

@Controller
public class ArticleController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired private ArticleService articleService;
    @Autowired private ArticleRepository articleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private ArticleTagService articleTagService;

    @GetMapping("/articles/new")
    public String showCreateForm(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("tags", articleTagService.getAllTags());
        return "frontEndModel/articleForm";
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
    @PostMapping("/articles")
    public String createArticle(
            @Valid @ModelAttribute("article") Article article,
            BindingResult result,
            @RequestParam(value = "tags", required = false) String tagsString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            Model model,
            Principal principal
    ) {
        if (principal == null) {
            return "redirect:/login";
        }

        User currentUser = userRepository.findByEmail(principal.getName()).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }

        article.setAuthor(currentUser);

        // Nếu có lỗi validate, trả về lại form với dữ liệu đã nhập
        if (result.hasErrors()) {
            logger.error("Validation errors: {}", result.getFieldErrors());
            model.addAttribute("tagsString", tagsString); // giữ lại dữ liệu tag đã nhập
            model.addAttribute("tags", articleTagService.getAllTags()); // cần thiết để hiển thị select/multiselect tag
            return "frontEndModel/articleForm";
        }

        // Nếu article đã chọn "published" nhưng chưa chọn ngày, thì gán mặc định
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

        article.setUpdatedAt(LocalDateTime.now());

        try {
            // Lưu article trước để lấy ID
            articleService.save(article);

            // Xử lý tags
            List<ArticleTag> tags = new ArrayList<>();
            if (tagsString != null && !tagsString.trim().isEmpty()) {
                String[] tagNames = tagsString.split(",");
                for (String tagName : tagNames) {
                    tags.add(articleTagService.createTagIfNotExists(article, tagName.trim()));
                }
                article.setTags(tags);
                articleService.save(article); // Cập nhật lại với tags
            }

            return "redirect:/articles/" + article.getArticleId();

        } catch (DataIntegrityViolationException e) {
            logger.error("Lỗi khi lưu article: {}", e.getMessage());
            model.addAttribute("error", "Dữ liệu không hợp lệ hoặc bị trùng lặp. Vui lòng kiểm tra lại.");
        } catch (Exception e) {
            logger.error("Lỗi không xác định khi lưu article: {}", e.getMessage());
            model.addAttribute("error", "Có lỗi xảy ra khi lưu bài viết. Vui lòng thử lại.");
        }

        // Nếu có exception, trả lại form với dữ liệu đã nhập
        model.addAttribute("tagsString", tagsString);
        model.addAttribute("tags", articleTagService.getAllTags());
        return "frontEndModel/articleForm";
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
