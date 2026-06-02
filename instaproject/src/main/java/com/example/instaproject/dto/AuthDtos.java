package com.example.instaproject.dto;

import lombok.Data;

public class AuthDtos {

    @Data
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
        private String fullName;
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class AuthResponse {
        private String token;
        private String username;
        private Long userId;

        public AuthResponse(String token, String username, Long userId) {
            this.token = token;
            this.username = username;
            this.userId = userId;
        }
    }
}
