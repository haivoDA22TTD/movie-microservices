package com.movie.comment.exception;

public class RateLimitExceededException extends RuntimeException {
    private final long resetTime;
    
    public RateLimitExceededException(String message, long resetTime) {
        super(message);
        this.resetTime = resetTime;
    }
    
    public long getResetTime() {
        return resetTime;
    }
}
