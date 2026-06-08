package com.divyansh.website;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog")
@CrossOrigin(origins = "*")
public class BlogController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    // Sab posts — public
    @GetMapping("/posts")
    public List<BlogPost> getAllPosts() {
        return blogRepository.findByPublishedTrueOrderByCreatedAtDesc();
    }

    // Single post
    @GetMapping("/posts/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        Optional<BlogPost> post = blogRepository.findById(id);
        if (post.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(post.get());
    }

    // Create post — sirf admin
    @PostMapping("/posts")
    public ResponseEntity<?> createPost(
            @RequestHeader("userId") Long userId,
            @RequestBody BlogPost post) {

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Please login first!");
            return ResponseEntity.status(401).body(error);
        }

        post.setAuthor(userOpt.get().getUsername());
        post.setAuthorId(userId);
        BlogPost saved = blogRepository.save(post);
        return ResponseEntity.ok(saved);
    }

    // Delete post — sirf admin
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Map<String, String>> deletePost(
            @RequestHeader("userId") Long userId,
            @PathVariable Long id) {

        Map<String, String> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            response.put("message", "Not authorized!");
            return ResponseEntity.status(401).body(response);
        }

        // Admin ya post author delete kar sake
        User user = userOpt.get();
        Optional<BlogPost> postOpt = blogRepository.findById(id);

        if (postOpt.isEmpty()) {
            response.put("message", "Post not found!");
            return ResponseEntity.notFound().build();
        }

        BlogPost post = postOpt.get();
        if (user.getRole() != User.Role.ADMIN && !post.getAuthor().equals(user.getUsername())) {
            response.put("message", "You can only delete your own posts!");
            return ResponseEntity.status(403).body(response);
        }

        blogRepository.deleteById(id);
        response.put("message", "Post deleted successfully!");
        return ResponseEntity.ok(response);
    }
}