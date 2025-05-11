package khiem.nhg.Project_64131000.repository;

import khiem.nhg.Project_64131000.model.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    List<Interaction> findByUserUserIdAndArticleArticleId(Long userId, Long articleId);
}