package khiem.nhg.Project_64131000.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ArticleTagId implements Serializable {

    @Column(name = "articleId")
    private Long articleId;

    @Column(name = "tags", length = 100)
    private String tags;

    // Constructors
    public ArticleTagId() {}
    public ArticleTagId(Long articleId, String tags) {
        this.articleId = articleId;
        this.tags = tags;
    }

    // Getters, Setters
    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    // hashCode & equals (BẮT BUỘC)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleTagId)) return false;
        ArticleTagId that = (ArticleTagId) o;
        return articleId.equals(that.articleId) && tags.equals(that.tags);
    }

    @Override
    public int hashCode() {
        return articleId.hashCode() + tags.hashCode();
    }
}
