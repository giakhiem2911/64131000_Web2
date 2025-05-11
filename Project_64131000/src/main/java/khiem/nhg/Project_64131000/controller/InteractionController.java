package khiem.nhg.Project_64131000.controller;

import khiem.nhg.Project_64131000.model.Interaction;
import khiem.nhg.Project_64131000.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/interactions")
public class InteractionController {
    @Autowired
    private InteractionService interactionService;

    @GetMapping
    public List<Interaction> getAllInteractions() {
        return interactionService.getAllInteractions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interaction> getInteractionById(@PathVariable Long id) {
        Optional<Interaction> interaction = interactionService.getInteractionById(id);
        return interaction.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}/article/{articleId}")
    public List<Interaction> getInteractionsByUserAndArticle(@PathVariable Long userId, @PathVariable Long articleId) {
        return interactionService.getInteractionsByUserAndArticle(userId, articleId);
    }

    @PostMapping
    public Interaction createInteraction(@RequestBody Interaction interaction) {
        return interactionService.createInteraction(interaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Interaction> updateInteraction(@PathVariable Long id, @RequestBody Interaction interactionDetails) {
        Interaction updatedInteraction = interactionService.updateInteraction(id, interactionDetails);
        if (updatedInteraction != null) {
            return ResponseEntity.ok(updatedInteraction);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInteraction(@PathVariable Long id) {
        interactionService.deleteInteraction(id);
        return ResponseEntity.noContent().build();
    }
}