package com.movie.comment.controller;

import com.movie.comment.model.Comment;
import com.movie.comment.repository.CommentRepository;
import com.movie.comment.service.RateLimitService;
import com.movie.comment.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CommentController {
    
    private final CommentRepository commentRepository;
    private final RateLimitService rateLimitService;
    
    @GetMapping("/movie/{movieId}")
    @Cacheable(value = "movieComments", key = "#movieId")
    public List<Comment> getCommentsByMovie(@PathVariable Long movieId) {
        return commentRepository.findByMovieIdOrderByCreatedAtDesc(movieId);
    }
    
    @GetMapping("/user/{email}")
    @Cacheable(value = "userComments", key = "#email")
    public List<Comment> getCommentsByUser(@PathVariable String email) {
        return commentRepository.findByUserEmailOrderByCreatedAtDesc(email);
    }
    
    @GetMapping("/rate-limit/{email}")
    public Map<String, Object> getRateLimitInfo(@PathVariable String email) {
        return Map.of(
            "remaining", rateLimitService.getRemainingRequests(email),
            "resetTime", rateLimitService.getResetTime(email)
        );
    }
    
    @PostMapping
    @CacheEvict(value = {"movieComments", "userComments"}, allEntries = true)
    public Comment createComment(@RequestBody Comment comment) {
        // Kiểm tra rate limit
        if (!rateLimitService.isAllowed(comment.getUserEmail())) {
            long resetTime = rateLimitService.getResetTime(comment.getUserEmail());
            throw new RateLimitExceededException(
                "Bạn đã bình luận quá nhiều. Vui lòng đợi " + resetTime + " giây.",
                resetTime
            );
        }
        
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }
    
    @PutMapping("/{id}")
    @CacheEvict(value = {"movieComments", "userComments"}, allEntries = true)
    public Comment updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        Comment existing = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comment not found"));
        existing.setContent(comment.getContent());
        existing.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(existing);
    }
    
    @DeleteMapping("/{id}")
    @CacheEvict(value = {"movieComments", "userComments"}, allEntries = true)
    public void deleteComment(@PathVariable Long id) {
        commentRepository.deleteById(id);
    }
}
