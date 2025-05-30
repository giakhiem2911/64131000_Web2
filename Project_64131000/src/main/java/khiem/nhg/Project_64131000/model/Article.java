package khiem.nhg.Project_64131000.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "articleId")
    private Long articleId;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    @Column(name = "content", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String content;

    @NotBlank(message = "Chuyên mục không được để trống")
    @Column(name = "category", nullable = false)
    private String category;

    @ManyToOne
    @JoinColumn(name = "authorId", nullable = false)
    @NotNull(message = "Tác giả không được để trống")
    private User author;

    @NotBlank(message = "Trạng thái không được để trống")
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "views", columnDefinition = "BIGINT DEFAULT 0")
    private Long views;

    @NotNull(message = "Ngày cập nhật không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "publishedAt")
    private LocalDateTime publishedAt;

    @Column(name = "imageUrl")
    private String imageUrl;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ArticleTag> tags;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<Interaction> interactions;

    @Transient
    private long likeCount;

    // Getters và Setters
    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getViews() { return views; }
    public void setViews(Long views) { this.views = views; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String image) { this.imageUrl = image; }

    public List<ArticleTag> getTags() { return tags; }
    public void setTags(List<ArticleTag> tags) { this.tags = tags; }

    public List<Interaction> getInteractions() { return interactions; }
    public void setInteractions(List<Interaction> interactions) { this.interactions = interactions; }

    public long getLikeCount() { return likeCount; }
    public void setLikeCount(long likeCount) { this.likeCount = likeCount; }

    public Long getId() { return articleId; }
}