package com.movie.comment.repository;

import com.movie.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMovieIdOrderByCreatedAtDesc(Long movieId);
    List<Comment> findByUserEmailOrderByCreatedAtDesc(String userEmail);
}
