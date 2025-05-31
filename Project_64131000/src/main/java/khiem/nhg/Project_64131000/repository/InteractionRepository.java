package khiem.nhg.Project_64131000.repository;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.model.Interaction;
import khiem.nhg.Project_64131000.model.InteractionType;
import khiem.nhg.Project_64131000.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
	Optional<Interaction> findByUserAndArticle(User user, Article article);
	List<Interaction> findAllByArticle(Article article);
    List<Interaction> findByUserUserIdAndArticleArticleId(Long userId, Long articleId);
    Interaction findByUserUserIdAndArticleArticleIdAndType(Long userId, Long articleId, String type);
    boolean existsByArticleAndUserAndType(Article article, User user, InteractionType like);
    void deleteByUserAndArticleAndType(User user, Article article, InteractionType type);

}