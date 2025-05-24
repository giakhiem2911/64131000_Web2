package khiem.nhg.Project_64131000.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.model.ArticleTag;
import khiem.nhg.Project_64131000.model.ArticleTagId;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, ArticleTagId> {
    List<ArticleTag> findByArticleArticleId(Long articleId);
    List<ArticleTag> findAllByIdTags(String tag);   
    Optional<ArticleTag> findByIdTags(String tag); 
    void deleteAllByArticle(Article article);
    List<ArticleTag> findAllByIdIn(List<Long> ids);
}
