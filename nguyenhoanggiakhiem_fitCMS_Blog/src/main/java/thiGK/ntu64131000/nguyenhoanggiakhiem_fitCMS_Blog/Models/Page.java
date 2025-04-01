package thiGK.ntu64131000.nguyenhoanggiakhiem_fitCMS_Blog.Models;

public class Page {
    private Long id;
    private String pageName;
    private String keyword;
    private String content;
    private Long parentPageId;
    private String duThumbnailImage;

    // Constructors
    public Page() {}

    public Page(String pageName, String keyword, String content, Long parentPageId, String duThumbnailImage) {
        this.pageName = pageName;
        this.keyword = keyword;
        this.content = content;
        this.parentPageId = parentPageId;
        this.duThumbnailImage = duThumbnailImage;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParentPageId() {
        return parentPageId;
    }

    public void setParentPageId(Long parentPageId) {
        this.parentPageId = parentPageId;
    }

    public String getDuThumbnailImage() {
        return duThumbnailImage;
    }

    public void setDuThumbnailImage(String duThumbnailImage) {
        this.duThumbnailImage = duThumbnailImage;
    }
}