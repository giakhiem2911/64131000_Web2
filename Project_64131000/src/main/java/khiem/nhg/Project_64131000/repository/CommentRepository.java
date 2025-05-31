package khiem.nhg.Project_64131000.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import khiem.nhg.Project_64131000.model.Comment;
import khiem.nhg.Project_64131000.model.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleArticleId(Long articleId);
    List<Comment> findByArticleIdOrderByCreatedAtDesc(Long articleId);
    List<Comment> findTop5ByUserOrderByCreatedAtDesc(User user);
}
