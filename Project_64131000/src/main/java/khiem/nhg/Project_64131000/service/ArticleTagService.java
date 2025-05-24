package khiem.nhg.Project_64131000.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.model.ArticleTag;
import khiem.nhg.Project_64131000.model.ArticleTagId;
import khiem.nhg.Project_64131000.repository.ArticleTagRepository;

@Service
public class ArticleTagService {

    @Autowired
    private ArticleTagRepository articleTagRepository;

    public List<ArticleTag> findByTag(String tag) {
        return articleTagRepository.findAllByIdTags(tag);
    }

    public ArticleTag createTag(Article article, String tag) {
        ArticleTagId id = new ArticleTagId(article.getId(), tag);
        ArticleTag articleTag = new ArticleTag(article, tag);
        return articleTagRepository.save(articleTag);
    }
    
    public ArticleTag createTagIfNotExists(Article article, String tag) {
        ArticleTagId id = new ArticleTagId(article.getId(), tag);
        return articleTagRepository.findById(id)
                .orElseGet(() -> articleTagRepository.save(new ArticleTag(article, tag)));
    }
}
