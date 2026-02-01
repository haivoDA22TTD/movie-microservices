package com.movie.comment.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "movie_id", nullable = false)
    private Long movieId;
    
    @Column(name = "user_email", nullable = false)
    private String userEmail;
    
    @Column(name = "user_name", nullable = false)
    private String userName;
    
    @Column(name = "user_picture")
    private String userPicture;
    
    @Column(nullable = false, length = 1000)
    private String content;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
