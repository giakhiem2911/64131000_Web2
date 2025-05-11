package khiem.nhg.Project_64131000.service;

import khiem.nhg.Project_64131000.model.Interaction;
import khiem.nhg.Project_64131000.repository.InteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InteractionService {
    @Autowired
    private InteractionRepository interactionRepository;

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
}