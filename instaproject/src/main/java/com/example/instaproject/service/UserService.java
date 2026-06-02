package com.example.instaproject.service;

import com.example.instaproject.dto.AuthDtos;
import com.example.instaproject.models.Follow;
import com.example.instaproject.models.User;
import com.example.instaproject.repository.FollowRepository;
import com.example.instaproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(AuthDtos.RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        return userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public User getById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> searchUsers(String query) {
        return userRepository.findByUsernameContainingIgnoreCase(query);
    }

    @Transactional
    public String followOrUnfollow(User currentUser, Long targetUserId) {
        User target = getById(targetUserId);
        if (currentUser.getId().equals(targetUserId)) {
            throw new RuntimeException("Cannot follow yourself");
        }
        var existing = followRepository.findByFollowerAndFollowing(currentUser, target);
        if (existing.isPresent()) {
            followRepository.delete(existing.get());
            return "unfollowed";
        } else {
            Follow follow = new Follow();
            follow.setFollower(currentUser);
            follow.setFollowing(target);
            followRepository.save(follow);
            return "followed";
        }
    }

    public int getFollowersCount(User user) {
        return followRepository.countByFollowing(user);
    }

    public int getFollowingCount(User user) {
        return followRepository.countByFollower(user);
    }

    public boolean isFollowing(User currentUser, User target) {
        return followRepository.existsByFollowerAndFollowing(currentUser, target);
    }

    @Transactional
    public User updateProfile(User user, String bio, String fullName, String profilePicture) {
        user.setBio(bio);
        user.setFullName(fullName);
        user.setProfilePicture(profilePicture);
        return userRepository.save(user);
    }
}
