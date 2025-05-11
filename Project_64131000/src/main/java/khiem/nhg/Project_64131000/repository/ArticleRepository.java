package khiem.nhg.Project_64131000.repository;

import khiem.nhg.Project_64131000.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByAuthorUserId(Long userId);
}