package com.movie.auth.controller;

import com.movie.auth.model.User;
import com.movie.auth.repository.UserRepository;
import com.movie.auth.security.JwtUtil;
import com.movie.auth.service.JwtBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {
    
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final JwtBlacklistService jwtBlacklistService;
    
    @GetMapping("/user")
    public Map<String, Object> getUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            throw new RuntimeException("User not authenticated");
        }
        
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String picture = principal.getAttribute("picture");
        String googleId = principal.getAttribute("sub");
        
        User user = userRepository.findByGoogleId(googleId)
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setPicture(picture);
                newUser.setGoogleId(googleId);
                return userRepository.save(newUser);
            });
        
        String token = jwtUtil.generateToken(email, name);
        
        return Map.of(
            "token", token,
            "user", Map.of(
                "email", email,
                "name", name,
                "picture", picture
            )
        );
    }
    
    @GetMapping("/auth/success")
    public RedirectView authSuccess(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return new RedirectView("http://localhost:4200?error=auth_failed");
        }
        
        try {
            String email = principal.getAttribute("email");
            String name = principal.getAttribute("name");
            String picture = principal.getAttribute("picture");
            String googleId = principal.getAttribute("sub");
            
            // Lưu user vào database
            User user = userRepository.findByGoogleId(googleId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setPicture(picture);
                    newUser.setGoogleId(googleId);
                    return userRepository.save(newUser);
                });
            
            // Tạo JWT token
            String token = jwtUtil.generateToken(email, name);
            
            // Redirect về frontend với token và user info trong URL
            String redirectUrl = UriComponentsBuilder
                .fromUriString("http://localhost:4200")
                .queryParam("token", token)
                .queryParam("email", email)
                .queryParam("name", name)
                .queryParam("picture", picture)
                .build()
                .toUriString();
            
            return new RedirectView(redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("http://localhost:4200?error=auth_failed");
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                long secondsUntilExpiration = jwtUtil.getSecondsUntilExpiration(token);
                
                if (secondsUntilExpiration > 0) {
                    jwtBlacklistService.blacklistToken(token, secondsUntilExpiration);
                }
                
                return ResponseEntity.ok(Map.of(
                    "message", "Đăng xuất thành công",
                    "success", true
                ));
            }
            
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Token không hợp lệ",
                "success", false
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Lỗi khi đăng xuất: " + e.getMessage(),
                "success", false
            ));
        }
    }
    
    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                boolean isBlacklisted = jwtBlacklistService.isBlacklisted(token);
                boolean isExpired = jwtUtil.isTokenExpired(token);
                
                if (isBlacklisted) {
                    return ResponseEntity.status(401).body(Map.of(
                        "valid", false,
                        "message", "Token đã bị vô hiệu hóa"
                    ));
                }
                
                if (isExpired) {
                    return ResponseEntity.status(401).body(Map.of(
                        "valid", false,
                        "message", "Token đã hết hạn"
                    ));
                }
                
                String email = jwtUtil.getEmailFromToken(token);
                return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "email", email
                ));
            }
            
            return ResponseEntity.badRequest().body(Map.of(
                "valid", false,
                "message", "Token không hợp lệ"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "valid", false,
                "message", "Lỗi khi xác thực token: " + e.getMessage()
            ));
        }
    }
}
