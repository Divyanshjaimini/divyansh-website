package com.divyansh.website;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("userId") Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "User not found!");
            return ResponseEntity.badRequest().body(error);
        }
        User user = userOpt.get();
        Map<String, Object> profile = new HashMap<>();
        profile.put("id", user.getId());
        profile.put("username", user.getUsername());
        profile.put("email", user.getEmail());
        profile.put("role", user.getRole());
        profile.put("joinedOn", user.getCreatedAt());
        profile.put("profilePhoto", user.getProfilePhoto());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestHeader("userId") Long userId,
            @RequestBody Map<String, String> request) {

        Map<String, String> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            response.put("message", "User not found!");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userOpt.get();
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            response.put("message", "Purana password galat hai!");
            return ResponseEntity.badRequest().body(response);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        response.put("message", "Password successfully change ho gaya!");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<Map<String, String>> updateProfile(
            @RequestHeader("userId") Long userId,
            @RequestBody Map<String, String> request) {

        Map<String, String> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            response.put("message", "User not found!");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userOpt.get();
        String newUsername = request.get("username");

        if (newUsername != null && !newUsername.isEmpty()) {
            if (userRepository.existsByUsername(newUsername) &&
                !newUsername.equals(user.getUsername())) {
                response.put("message", "Ye username pehle se le liya gaya hai!");
                return ResponseEntity.badRequest().body(response);
            }
            user.setUsername(newUsername);
            userRepository.save(user);
            response.put("message", "Profile update ho gaya!");
            response.put("username", newUsername);
        }
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/upload-photo")
    public ResponseEntity<Map<String, String>> uploadPhoto(
            @RequestHeader("userId") Long userId,
            @RequestBody Map<String, String> request) {

        Map<String, String> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            response.put("message", "User not found!");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userOpt.get();
        user.setProfilePhoto(request.get("photo"));
        userRepository.save(user);

        response.put("message", "Photo uploaded successfully!");
        return ResponseEntity.ok(response);
    }
}