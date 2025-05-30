package khiem.nhg.Project_64131000.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Interactions")
public class Interaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interactionId")
    private Long interactionId;

    @ManyToOne
    @JoinColumn(name = "articleId", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private InteractionType type;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    public Long getInteractionId() { return interactionId; }
    public void setInteractionId(Long interactionId) { this.interactionId = interactionId; }
    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public InteractionType getType() { return type; }
    public void setType(InteractionType type) { this.type = type; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}