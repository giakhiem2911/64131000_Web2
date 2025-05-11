package khiem.nhg.Project_64131000.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import khiem.nhg.Project_64131000.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleArticleId(Long articleId);
}
