package com.divyansh.website;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // Sabhi users ki list (sirf admin ke liye)
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestHeader("userId") Long userId) {
        Optional<User> adminOpt = userRepository.findById(userId);

        if (adminOpt.isEmpty() || adminOpt.get().getRole() != User.Role.ADMIN) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Access Denied! Admins only.");
            return ResponseEntity.status(403).body(error);
        }

        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // Kisi bhi user ko ADMIN banana
    @PutMapping("/make-admin/{targetUserId}")
    public ResponseEntity<Map<String, String>> makeAdmin(
            @RequestHeader("userId") Long requesterId,
            @PathVariable Long targetUserId) {

        Map<String, String> response = new HashMap<>();

        Optional<User> requesterOpt = userRepository.findById(requesterId);
        if (requesterOpt.isEmpty() || requesterOpt.get().getRole() != User.Role.ADMIN) {
            response.put("message", "Access Denied! Admins only.");
            return ResponseEntity.status(403).body(response);
        }

        Optional<User> targetOpt = userRepository.findById(targetUserId);
        if (targetOpt.isEmpty()) {
            response.put("message", "User not found!");
            return ResponseEntity.badRequest().body(response);
        }

        User target = targetOpt.get();
        target.setRole(User.Role.ADMIN);
        userRepository.save(target);

        response.put("message", target.getUsername() + " is now an ADMIN!");
        return ResponseEntity.ok(response);
    }

    // User delete karna
    @DeleteMapping("/delete-user/{targetUserId}")
    public ResponseEntity<Map<String, String>> deleteUser(
            @RequestHeader("userId") Long requesterId,
            @PathVariable Long targetUserId) {

        Map<String, String> response = new HashMap<>();

        Optional<User> requesterOpt = userRepository.findById(requesterId);
        if (requesterOpt.isEmpty() || requesterOpt.get().getRole() != User.Role.ADMIN) {
            response.put("message", "Access Denied! Admins only.");
            return ResponseEntity.status(403).body(response);
        }

        userRepository.deleteById(targetUserId);
        response.put("message", "User deleted successfully!");
        return ResponseEntity.ok(response);
    }

    // Dashboard stats
    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestHeader("userId") Long userId) {
        Optional<User> adminOpt = userRepository.findById(userId);

        if (adminOpt.isEmpty() || adminOpt.get().getRole() != User.Role.ADMIN) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Access Denied! Admins only.");
            return ResponseEntity.status(403).body(error);
        }

        long totalUsers = userRepository.count();
        long totalAdmins = userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == User.Role.ADMIN)
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("totalAdmins", totalAdmins);
        stats.put("totalNormalUsers", totalUsers - totalAdmins);
        return ResponseEntity.ok(stats);
    }
}
