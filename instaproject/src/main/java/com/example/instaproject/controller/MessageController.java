package com.example.instaproject.controller;

import com.example.instaproject.models.User;
import com.example.instaproject.service.MessageService;
import com.example.instaproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getConversation(@PathVariable String username,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        User current = userService.getByUsername(userDetails.getUsername());
        User other = userService.getByUsername(username);
        return ResponseEntity.ok(messageService.getConversation(current, other));
    }

    @PostMapping("/{username}")
    public ResponseEntity<?> sendMessage(@PathVariable String username,
                                         @RequestBody Map<String, String> body,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        User current = userService.getByUsername(userDetails.getUsername());
        User receiver = userService.getByUsername(username);
        return ResponseEntity.ok(messageService.sendMessage(current, receiver, body.get("content")));
    }

    @GetMapping("/{username}/unread")
    public ResponseEntity<?> getUnreadCount(@PathVariable String username,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        User current = userService.getByUsername(userDetails.getUsername());
        User other = userService.getByUsername(username);
        int count = messageService.getUnreadCount(other, current);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }
}
