package khiem.nhg.Project_64131000.repository;

import khiem.nhg.Project_64131000.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE a.status = 'PUBLISHED' ORDER BY a.views DESC, a.publishedAt DESC")
    List<Article> findFeaturedArticles();
}