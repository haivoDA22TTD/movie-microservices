package com.movie.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RateLimitService {
    
    private final RedisTemplate<String, String> redisTemplate;
    
    @Value("${rate-limit.max-requests:3}")
    private int maxRequests;
    
    @Value("${rate-limit.window-seconds:60}")
    private int windowSeconds;
    
    public boolean isAllowed(String userEmail) {
        String key = "rate_limit:comment:" + userEmail;
        
        // Lấy số lượng request hiện tại
        String countStr = redisTemplate.opsForValue().get(key);
        int count = countStr != null ? Integer.parseInt(countStr) : 0;
        
        if (count >= maxRequests) {
            // Đã vượt quá giới hạn
            return false;
        }
        
        // Tăng counter
        redisTemplate.opsForValue().increment(key);
        
        // Set TTL nếu là request đầu tiên
        if (count == 0) {
            redisTemplate.expire(key, windowSeconds, TimeUnit.SECONDS);
        }
        
        return true;
    }
    
    public int getRemainingRequests(String userEmail) {
        String key = "rate_limit:comment:" + userEmail;
        String countStr = redisTemplate.opsForValue().get(key);
        int count = countStr != null ? Integer.parseInt(countStr) : 0;
        return Math.max(0, maxRequests - count);
    }
    
    public long getResetTime(String userEmail) {
        String key = "rate_limit:comment:" + userEmail;
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return ttl != null ? ttl : 0;
    }
}
