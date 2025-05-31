package khiem.nhg.Project_64131000.model;

public class LikeRequest {
    private Long userId;
    private Long articleId;

    // Constructors
    public LikeRequest() {}
    public LikeRequest(Long userId, Long articleId) {
        this.userId = userId;
        this.articleId = articleId;
    }

    // Getters và Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
}
