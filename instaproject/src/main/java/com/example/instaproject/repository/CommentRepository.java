package com.example.instaproject.repository;

import com.example.instaproject.models.Comment;
import com.example.instaproject.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
    int countByPost(Post post);
}
