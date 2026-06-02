package com.example.instaproject.controller;

import com.example.instaproject.dto.PostDtos;
import com.example.instaproject.models.User;
import com.example.instaproject.service.PostService;
import com.example.instaproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @GetMapping("/feed")
    public ResponseEntity<?> getFeed(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByUsername(userDetails.getUsername());
        return ResponseEntity.ok(postService.getFeed(user));
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostDtos.CreatePostRequest request,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByUsername(userDetails.getUsername());
        return ResponseEntity.ok(postService.createPost(user, request));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByUsername(userDetails.getUsername());
        String result = postService.toggleLike(user, id);
        return ResponseEntity.ok(Map.of("status", result));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id,
                                        @RequestBody PostDtos.CommentRequest request,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByUsername(userDetails.getUsername());
        return ResponseEntity.ok(postService.addComment(user, id, request.getText()));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getComments(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByUsername(userDetails.getUsername());
        postService.deletePost(user, id);
        return ResponseEntity.ok(Map.of("message", "Post deleted"));
    }
}
