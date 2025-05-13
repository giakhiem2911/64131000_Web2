package khiem.nhg.Project_64131000.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ArticlesImages")
public class ArticleImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imageId")
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "articleId", nullable = false)
    private Article article;

    @Column(name = "imageUrl", nullable = false)
    private String imageUrl;

    // Getters and Setters
    public Long getImageId() { return imageId; }
    public void setImageId(Long imageId) { this.imageId = imageId; }
    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}