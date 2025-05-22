package khiem.nhg.Project_64131000.repository;

import khiem.nhg.Project_64131000.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE a.status = 'PUBLISHED' ORDER BY a.views DESC, a.publishedAt DESC")
    List<Article> findFeaturedArticles();
    @Query("SELECT a FROM Article a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(a.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Article> searchByKeyword(@Param("keyword") String keyword);
    @Query("SELECT a FROM Article a WHERE a.category = :category AND a.status = 'PUBLISHED' ORDER BY a.publishedAt DESC")
    List<Article> findByCategory(@Param("category") String category);

    @Query("SELECT a FROM Article a WHERE a.status = 'PUBLISHED' ORDER BY a.publishedAt DESC")
    List<Article> findTop5ByOrderByPublishedAtDesc();

    Optional<Article> findById(Long id);
    List<Article> findByCategoryIgnoreCase(String category);
}