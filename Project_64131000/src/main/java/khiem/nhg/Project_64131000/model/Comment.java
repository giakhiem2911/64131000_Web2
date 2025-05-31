package khiem.nhg.Project_64131000.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

    public Long getCommentId() {
		return commentId;
	}
    public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
    public String getContent() {
		return content;
	}
    public void setContent(String content) {
		this.content = content;
	}
    public LocalDateTime getCreatedAt() {
		return createdAt;
	}
    public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
    public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

}
