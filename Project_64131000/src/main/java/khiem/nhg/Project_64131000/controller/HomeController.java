package khiem.nhg.Project_64131000.controller;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Article> articles = articleRepository.findAll();

        for (Article article : articles) {
            long likeCount = article.getInteractions()
                    .stream()
                    .filter(i -> "like".equalsIgnoreCase(i.getType()))
                    .count();
            article.setLikeCount(likeCount);
        }

        model.addAttribute("articles", articles);
        return "frontEndModel/index";
    }
}
