package khiem.nhg.Project_64131000.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.model.ArticleTag;
import khiem.nhg.Project_64131000.model.ArticleTagId;
import khiem.nhg.Project_64131000.repository.ArticleRepository;
import khiem.nhg.Project_64131000.repository.ArticleTagRepository;

@Service
public class ArticleTagService {

    @Autowired
    private ArticleTagRepository articleTagRepository;
    @Autowired
    private ArticleRepository articleRepository;
    public List<ArticleTag> getAllTags() {
        return articleTagRepository.findAll(); 
    }
    public List<ArticleTag> findByTag(String tag) {
        return articleTagRepository.findAllByIdTags(tag);
    }

    public ArticleTag createTag(Article article, String tag) {
        ArticleTagId id = new ArticleTagId(article.getId(), tag);
        ArticleTag articleTag = new ArticleTag(article, tag);
        return articleTagRepository.save(articleTag);
    }
    
    public ArticleTag createTagIfNotExists(Article article, String tagName) {
        // Kiểm tra article và author
        if (article == null || article.getAuthor() == null || article.getAuthor().getUserId() == null) {
            throw new IllegalArgumentException("Bài viết hoặc tác giả không hợp lệ.");
        }

        // Lưu article nếu chưa có ID
        if (article.getId() == null) {
            article = articleRepository.save(article);
        }

        // Tìm hoặc tạo tag
        Optional<ArticleTag> existingTag = articleTagRepository.findByTagsAndArticle(tagName, article);
        if (existingTag.isPresent()) {
            return existingTag.get();
        }

        ArticleTag tag = new ArticleTag(article, tagName);
        return articleTagRepository.save(tag);
    }
    public void deleteTagsByArticle(Article article) {
        articleTagRepository.deleteAllByArticle(article);
    }

}