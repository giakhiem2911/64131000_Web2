package khiem.nhg.Project_64131000.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.model.ArticleTag;
import khiem.nhg.Project_64131000.model.ArticleTagId;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, ArticleTagId> {
    List<ArticleTag> findByArticleArticleId(Long articleId);
    List<ArticleTag> findAllByIdTags(String tag);   
    void deleteAllByArticle(Article article);
    List<ArticleTag> findAllByIdIn(List<ArticleTagId> ids);
    @Query("SELECT t FROM ArticleTag t WHERE t.id.tags = :tags AND t.article = :article")
    Optional<ArticleTag> findByTagsAndArticle(@Param("tags") String tags, @Param("article") Article article);
}