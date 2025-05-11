package khiem.nhg.Project_64131000.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ArticlesImages")
public class ArticleImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

    // Getters & Setters
    public Long getImageId() {
		return imageId;
	}
    public void setImageId(Long imageId) {
		this.imageId = imageId;
	}
    public String getImageUrl() {
		return imageUrl;
	}
    public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
