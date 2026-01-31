package com.movie.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {
    
    private final RedisTemplate<String, String> redisTemplate;
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";
    
    /**
     * Thêm token vào blacklist
     * @param token JWT token
     * @param expirationSeconds Thời gian hết hạn (giây)
     */
    public void blacklistToken(String token, long expirationSeconds) {
        String key = BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(key, "blacklisted", expirationSeconds, TimeUnit.SECONDS);
    }
    
    /**
     * Kiểm tra token có trong blacklist không
     * @param token JWT token
     * @return true nếu token bị blacklist
     */
    public boolean isBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    
    /**
     * Xóa token khỏi blacklist (nếu cần)
     * @param token JWT token
     */
    public void removeFromBlacklist(String token) {
        String key = BLACKLIST_PREFIX + token;
        redisTemplate.delete(key);
    }
}
