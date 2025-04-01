package thiGK.ntu64131000.nguyenhoanggiakhiem_fitCMS_Blog.Models;


public class Post {
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private String duThumbnailImage;

    // Constructors
    public Post() {}

    public Post(String title, String content, Long categoryId, String duThumbnailImage) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.duThumbnailImage = duThumbnailImage;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDuThumbnailImage() {
        return duThumbnailImage;
    }

    public void setDuThumbnailImage(String duThumbnailImage) {
        this.duThumbnailImage = duThumbnailImage;
    }
}