package com.example.instaproject.dto;

import lombok.Data;

public class PostDtos {

    @Data
    public static class CreatePostRequest {
        private String imageUrl;
        private String caption;
    }

    @Data
    public static class PostResponse {
        private Long id;
        private String imageUrl;
        private String caption;
        private String authorUsername;
        private String authorProfilePicture;
        private int likesCount;
        private int commentsCount;
        private boolean likedByCurrentUser;
        private String createdAt;
    }

    @Data
    public static class CommentRequest {
        private String text;
    }
}
