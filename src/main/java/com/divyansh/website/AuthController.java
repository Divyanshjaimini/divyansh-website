package com.divyansh.website;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private OtpService otpService;

    // Generate OTP — Frontend EmailJS se bhejega
    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        String email = request.get("email");

        if (userRepository.existsByEmail(email)) {
            response.put("message", "Email already registered!");
            return ResponseEntity.badRequest().body(response);
        }

        String otp = otpService.generateOtp(email);
        response.put("message", "OTP generated!");
        response.put("otp", otp); // Frontend ko OTP denge — EmailJS se bhejega
        return ResponseEntity.ok(response);
    }

    // Register — OTP verify karke
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();

        String username = request.get("username");
        String email = request.get("email");
        String password = request.get("password");
        String otp = request.get("otp");

        if (userRepository.existsByEmail(email)) {
            response.put("message", "Email already registered!");
            return ResponseEntity.badRequest().body(response);
        }

        if (userRepository.existsByUsername(username)) {
            response.put("message", "Username already taken!");
            return ResponseEntity.badRequest().body(response);
        }

        if (!otpService.verifyOtp(email, otp)) {
            response.put("message", "Invalid or expired OTP!");
            return ResponseEntity.badRequest().body(response);
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.USER);
        userRepository.save(user);

        response.put("message", "Registration successful!");
        response.put("username", username);
        return ResponseEntity.ok(response);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        String email = request.get("email");
        String password = request.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("message", "User not found!");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            response.put("message", "Wrong password!");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("message", "Login successful!");
        response.put("username", user.getUsername());
        response.put("role", user.getRole().toString());
        response.put("userId", user.getId().toString());
        return ResponseEntity.ok(response);
    }
    
 // Forgot Password — OTP bhejo
    @PostMapping("/forgot-password/send-otp")
    public ResponseEntity<Map<String, String>> forgotPasswordSendOtp(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        String email = request.get("email");
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("message", "Email not registered!");
            return ResponseEntity.badRequest().body(response);
        }
        String otp = otpService.generateOtp(email);
        response.put("message", "OTP generated!");
        response.put("otp", otp);
        return ResponseEntity.ok(response);
    }

    // Reset Password
    @PostMapping("/forgot-password/reset")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        if (!otpService.verifyOtp(email, otp)) {
            response.put("message", "Invalid or expired OTP!");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("message", "User not found!");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        response.put("message", "Password reset successful!");
        return ResponseEntity.ok(response);
    }
}