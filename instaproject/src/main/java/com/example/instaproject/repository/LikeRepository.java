package com.example.instaproject.repository;

import com.example.instaproject.models.Like;
import com.example.instaproject.models.Post;
import com.example.instaproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    int countByPost(Post post);
    boolean existsByUserAndPost(User user, Post post);
}
