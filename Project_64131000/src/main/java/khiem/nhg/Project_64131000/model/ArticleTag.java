package khiem.nhg.Project_64131000.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "ArticlesTags")
public class ArticleTag {

    @EmbeddedId
    private ArticleTagId id;

    @ManyToOne
    @MapsId("articleId") // Map với field trong @Embeddable
    @JoinColumn(name = "articleId", insertable = false, updatable = false)
    private Article article;

    // Constructors
    public ArticleTag() {}
    public ArticleTag(Article article, String tags) {
        this.article = article;
        this.id = new ArticleTagId(article.getId(), tags);
    }

    // Getters & Setters
    public ArticleTagId getId() { return id; }
    public void setId(ArticleTagId id) { this.id = id; }

    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }

    public String getTags() { return id != null ? id.getTags() : null; }
    public void setTags(String tags) {
        if (this.id != null) this.id.setTags(tags);
    }
}
