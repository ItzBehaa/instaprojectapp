package com.example.instaproject.controller;

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
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByUsername(userDetails.getUsername());
        return ResponseEntity.ok(buildProfile(user, user));
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getProfile(@PathVariable String username,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        User current = userService.getByUsername(userDetails.getUsername());
        User target = userService.getByUsername(username);
        return ResponseEntity.ok(buildProfile(target, current));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String q) {
        return ResponseEntity.ok(userService.searchUsers(q));
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<?> follow(@PathVariable Long id,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        User current = userService.getByUsername(userDetails.getUsername());
        String result = userService.followOrUnfollow(current, id);
        return ResponseEntity.ok(Map.of("status", result));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> body,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByUsername(userDetails.getUsername());
        User updated = userService.updateProfile(
            user,
            body.getOrDefault("bio", user.getBio()),
            body.getOrDefault("fullName", user.getFullName()),
            body.getOrDefault("profilePicture", user.getProfilePicture())
        );
        return ResponseEntity.ok(buildProfile(updated, updated));
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<?> getUserPosts(@PathVariable String username,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        User current = userService.getByUsername(userDetails.getUsername());
        User target = userService.getByUsername(username);
        return ResponseEntity.ok(postService.getUserPosts(target, current));
    }

    private Map<String, Object> buildProfile(User target, User current) {
        return Map.of(
            "id", target.getId(),
            "username", target.getUsername(),
            "fullName", target.getFullName() != null ? target.getFullName() : "",
            "bio", target.getBio() != null ? target.getBio() : "",
            "profilePicture", target.getProfilePicture() != null ? target.getProfilePicture() : "",
            "followersCount", userService.getFollowersCount(target),
            "followingCount", userService.getFollowingCount(target),
            "isFollowing", userService.isFollowing(current, target)
        );
    }
}
