package khiem.nhg.Project_64131000.service;

import khiem.nhg.Project_64131000.model.Article;
import khiem.nhg.Project_64131000.model.Interaction;
import khiem.nhg.Project_64131000.model.InteractionType;
import khiem.nhg.Project_64131000.model.User;
import khiem.nhg.Project_64131000.repository.ArticleRepository;
import khiem.nhg.Project_64131000.repository.InteractionRepository;
import khiem.nhg.Project_64131000.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InteractionService {
    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void likeArticle(Long articleId, String userEmail) {
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        Optional<Article> articleOpt = articleRepository.findById(articleId);

        if (userOpt.isPresent() && articleOpt.isPresent()) {
            User user = userOpt.get();
            Article article = articleOpt.get();

            // Kiểm tra nếu đã like rồi thì không làm gì nữa
            Optional<Interaction> existing = interactionRepository.findByUserAndArticle(user, article);
            if (existing.isPresent()) return;

            Interaction interaction = new Interaction();
            interaction.setUser(user);
            interaction.setArticle(article);
            interaction.setType(InteractionType.LIKE);
            interaction.setCreatedAt(LocalDateTime.now());
            interactionRepository.save(interaction);

            // Tăng lượt thích lên
            article.setLikeCount(article.getLikeCount() + 1);
            articleRepository.save(article);
        }
    }
    
    public List<Interaction> findAllByArticle(Article article) {
        return interactionRepository.findAllByArticle(article);
    }

    public List<Interaction> getAllInteractions() {
        return interactionRepository.findAll();
    }

    public Optional<Interaction> getInteractionById(Long id) {
        return interactionRepository.findById(id);
    }

    public List<Interaction> getInteractionsByUserAndArticle(Long userId, Long articleId) {
        return interactionRepository.findByUserUserIdAndArticleArticleId(userId, articleId);
    }

    public Interaction createInteraction(Interaction interaction) {
        return interactionRepository.save(interaction);
    }

    public Interaction updateInteraction(Long id, Interaction interactionDetails) {
        Optional<Interaction> optionalInteraction = interactionRepository.findById(id);
        if (optionalInteraction.isPresent()) {
            Interaction interaction = optionalInteraction.get();
            interaction.setUser(interactionDetails.getUser());
            interaction.setArticle(interactionDetails.getArticle());
            interaction.setType(interactionDetails.getType());
            interaction.setCreatedAt(interactionDetails.getCreatedAt());
            return interactionRepository.save(interaction);
        }
        return null;
    }

    public void deleteInteraction(Long id) {
        interactionRepository.deleteById(id);
    }

    public void unlikeArticle(Long articleId, String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        Optional<Article> optionalArticle = articleRepository.findById(articleId);

        if (optionalUser.isEmpty() || optionalArticle.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy người dùng hoặc bài viết.");
        }

        User user = optionalUser.get();
        Article article = optionalArticle.get();

        // Tìm Interaction giữa user và article
        Optional<Interaction> optionalInteraction = interactionRepository.findByUserAndArticle(user, article);

        if (optionalInteraction.isPresent()) {
            interactionRepository.delete(optionalInteraction.get());
        }
    }

}