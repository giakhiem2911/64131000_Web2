package khiem.nhg.Project_64131000.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.service.ArticleService;

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
        return "frontEndModel/admin/article/form";
    }

    @PostMapping("/articles/save")
    public String saveArticle(@ModelAttribute Article article, 
                              @RequestParam("publishedAt") String publishedAtStr,
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

        articleService.save(article);
        return "redirect:/admin/articles";
    }

    @GetMapping("/articles/edit/{id}")
    public String editArticle(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        if (article == null) return "redirect:/admin/articles";
        model.addAttribute("article", article);
        return "frontEndModel/admin/article/form";
    }

    @GetMapping("/articles/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.deleteById(id);
        return "redirect:/admin/articles";
    }
}