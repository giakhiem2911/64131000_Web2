package khiem.nhg.Project_64131000.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ArticlesTags")
public class ArticleTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tags;

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

    // Getters & Setters
    public Long getId() {
		return id;
	}
    public void setId(Long id) {
		this.id = id;
	}
    public String getTags() {
		return tags;
	}
    public void setTags(String tags) {
		this.tags = tags;
	}
}
