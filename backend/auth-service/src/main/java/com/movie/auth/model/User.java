package com.movie.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String name;
    private String picture;
    private String googleId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
