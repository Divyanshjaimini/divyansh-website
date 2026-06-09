package com.divyansh.website;



import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    @Autowired
    private UserRepository userRepository;

    // PUBLIC chat
    @MessageMapping("/chat.public")
    @SendTo("/topic/public")
    public ChatMessage publicMessage(ChatMessage message) {
        message.setType("PUBLIC");
        chatMessageRepository.save(message);
        return message;
    }

    // PRIVATE chat
    @MessageMapping("/chat.private")
    public void privateMessage(ChatMessage message) {
        message.setType("PRIVATE");
        chatMessageRepository.save(message);
        messagingTemplate.convertAndSendToUser(
            message.getRecipient(), "/queue/private", message);
        messagingTemplate.convertAndSendToUser(
            message.getSender(), "/queue/private", message);
    }

    // Public chat history
    @GetMapping("/api/chat/public-history")
    public List<ChatMessage> getPublicHistory() {
        return chatMessageRepository.findByTypeOrderByTimestampAsc("PUBLIC");
    }

    // Private chat history
    @GetMapping("/api/chat/private-history")
    public List<ChatMessage> getPrivateHistory(
            @RequestParam String user1,
            @RequestParam String user2) {
        return chatMessageRepository
            .findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(
                user1, user2, user1, user2);
    }

    // Online users ke liye
    @MessageMapping("/chat.join")
    @SendTo("/topic/public")
    public ChatMessage joinChat(ChatMessage message) {
        message.setType("JOIN");
        return message;
    }
    
    @MessageMapping("/chat.leave")
    @SendTo("/topic/public")
    public ChatMessage leaveChat(ChatMessage message) {
        message.setType("LEAVE");
        return message;
    }
    
 // Message delete karo
    @DeleteMapping("/api/chat/message/{id}")
    public ResponseEntity<Map<String, String>> deleteMessage(
            @RequestHeader("userId") Long userId,
            @PathVariable Long id) {

        Map<String, String> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            response.put("message", "Not authorized!");
            return ResponseEntity.status(403).body(response);
        }

        chatMessageRepository.deleteById(id);
        response.put("message", "Message deleted!");
        return ResponseEntity.ok(response);
    }
    
}
