package com.movie.comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleRateLimitExceeded(RateLimitExceededException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "RATE_LIMIT_EXCEEDED");
        response.put("message", ex.getMessage());
        response.put("resetTime", ex.getResetTime());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
    }
}
