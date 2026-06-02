package com.example.instaproject.service;

import com.example.instaproject.dto.PostDtos;
import com.example.instaproject.models.*;
import com.example.instaproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Post createPost(User author, PostDtos.CreatePostRequest request) {
        Post post = new Post();
        post.setImageUrl(request.getImageUrl());
        post.setCaption(request.getCaption());
        post.setAuthor(author);
        return postRepository.save(post);
    }

    public List<PostDtos.PostResponse> getFeed(User user) {
        return postRepository.findFeedForUser(user).stream()
            .map(p -> toResponse(p, user))
            .collect(Collectors.toList());
    }

    public List<PostDtos.PostResponse> getUserPosts(User profileUser, User currentUser) {
        return postRepository.findByAuthorOrderByCreatedAtDesc(profileUser).stream()
            .map(p -> toResponse(p, currentUser))
            .collect(Collectors.toList());
    }

    @Transactional
    public String toggleLike(User user, Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));
        var existing = likeRepository.findByUserAndPost(user, post);
        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            return "unliked";
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
            return "liked";
        }
    }

    @Transactional
    public Comment addComment(User user, Long postId, String text) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));
        Comment comment = new Comment();
        comment.setText(text);
        comment.setAuthor(user);
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    public List<Comment> getComments(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));
        return commentRepository.findByPostOrderByCreatedAtAsc(post);
    }

    @Transactional
    public void deletePost(User user, Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized");
        }
        postRepository.delete(post);
    }

    private PostDtos.PostResponse toResponse(Post post, User currentUser) {
        PostDtos.PostResponse resp = new PostDtos.PostResponse();
        resp.setId(post.getId());
        resp.setImageUrl(post.getImageUrl());
        resp.setCaption(post.getCaption());
        resp.setAuthorUsername(post.getAuthor().getUsername());
        resp.setAuthorProfilePicture(post.getAuthor().getProfilePicture());
        resp.setLikesCount(likeRepository.countByPost(post));
        resp.setCommentsCount(commentRepository.countByPost(post));
        resp.setLikedByCurrentUser(likeRepository.existsByUserAndPost(currentUser, post));
        resp.setCreatedAt(post.getCreatedAt().toString());
        return resp;
    }
}
