package com.divyansh.website;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stories")
@CrossOrigin(origins = "*")
public class StoryController {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    // Sab active stories
    @GetMapping
    public List<Story> getStories() {
        List<Story> all = storyRepository
            .findByExpiresAtAfterOrderByCreatedAtDesc(LocalDateTime.now());
        return all.stream()
            .filter(s -> !s.isExpired())
            .collect(Collectors.toList());
    }

    // Story create karo
    @PostMapping
    public ResponseEntity<?> createStory(
            @RequestHeader("userId") Long userId,
            @RequestBody Story story) {

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            Map<String, String> err = new HashMap<>();
            err.put("message", "Please login!");
            return ResponseEntity.status(401).body(err);
        }

        User user = userOpt.get();
        story.setAuthor(user.getUsername());
        story.setAuthorId(userId);
        story.setCreatedAt(LocalDateTime.now());
        story.setExpiresAt(LocalDateTime.now().plusHours(24));

        Story saved = storyRepository.save(story);
        return ResponseEntity.ok(saved);
    }

    // Story delete karo
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStory(
            @RequestHeader("userId") Long userId,
            @PathVariable Long id) {

        Map<String, String> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            response.put("message", "Not authorized!");
            return ResponseEntity.status(401).body(response);
        }

        Optional<Story> storyOpt = storyRepository.findById(id);
        if (storyOpt.isEmpty()) {
            response.put("message", "Story not found!");
            return ResponseEntity.notFound().build();
        }

        Story story = storyOpt.get();
        User user = userOpt.get();

        if (!story.getAuthorId().equals(userId) && user.getRole() != User.Role.ADMIN) {
            response.put("message", "Not authorized!");
            return ResponseEntity.status(403).body(response);
        }

        storyRepository.deleteById(id);
        response.put("message", "Story deleted!");
        return ResponseEntity.ok(response);
    }
}
