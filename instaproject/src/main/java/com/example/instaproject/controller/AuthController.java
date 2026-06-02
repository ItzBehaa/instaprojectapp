package com.example.instaproject.controller;

import com.example.instaproject.dto.AuthDtos;
import com.example.instaproject.models.User;
import com.example.instaproject.service.JwtService;
import com.example.instaproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthDtos.RegisterRequest request) {
        User user = userService.register(request);
        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, user.getUsername(), user.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDtos.LoginRequest request) {
        User user = userService.getByUsername(request.getUsername());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthDtos.AuthResponse(token, user.getUsername(), user.getId()));
    }
}
