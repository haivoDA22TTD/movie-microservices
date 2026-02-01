package com.movie.comment.grpc;

import com.movie.comment.model.Comment;
import com.movie.comment.repository.CommentRepository;
import com.movie.comment.service.RateLimitService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class CommentGrpcService extends CommentServiceGrpc.CommentServiceImplBase {
    
    private final CommentRepository commentRepository;
    private final RateLimitService rateLimitService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    @Override
    public void getCommentsByMovie(GetCommentsByMovieRequest request, 
                                   StreamObserver<CommentMessage> responseObserver) {
        log.info("gRPC: Getting comments for movie {}", request.getMovieId());
        
        try {
            List<Comment> comments = commentRepository.findByMovieIdOrderByCreatedAtDesc(request.getMovieId());
            
            // Stream each comment
            for (Comment comment : comments) {
                CommentMessage message = CommentMessage.newBuilder()
                    .setId(comment.getId())
                    .setMovieId(comment.getMovieId())
                    .setUserEmail(comment.getUserEmail())
                    .setUserName(comment.getUserName())
                    .setUserPicture(comment.getUserPicture() != null ? comment.getUserPicture() : "")
                    .setContent(comment.getContent())
                    .setCreatedAt(comment.getCreatedAt().format(formatter))
                    .setUpdatedAt(comment.getUpdatedAt().format(formatter))
                    .build();
                    
                responseObserver.onNext(message);
            }
            
            responseObserver.onCompleted();
            log.info("gRPC: Streamed {} comments", comments.size());
            
        } catch (Exception e) {
            log.error("Error getting comments: {}", e.getMessage());
            responseObserver.onError(e);
        }
    }
    
    @Override
    public void createComment(CreateCommentRequest request, 
                             StreamObserver<CreateCommentResponse> responseObserver) {
        log.info("gRPC: Creating comment for movie {} by {}", request.getMovieId(), request.getUserEmail());
        
        try {
            // Check rate limit
            if (!rateLimitService.isAllowed(request.getUserEmail())) {
                long resetTime = rateLimitService.getResetTime(request.getUserEmail());
                CreateCommentResponse response = CreateCommentResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Bạn đã bình luận quá nhiều. Vui lòng đợi " + resetTime + " giây.")
                    .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
            
            // Create comment
            Comment comment = new Comment();
            comment.setMovieId(request.getMovieId());
            comment.setUserEmail(request.getUserEmail());
            comment.setUserName(request.getUserName());
            comment.setUserPicture(request.getUserPicture());
            comment.setContent(request.getContent());
            comment.setCreatedAt(LocalDateTime.now());
            comment.setUpdatedAt(LocalDateTime.now());
            
            Comment saved = commentRepository.save(comment);
            
            CommentMessage commentMessage = CommentMessage.newBuilder()
                .setId(saved.getId())
                .setMovieId(saved.getMovieId())
                .setUserEmail(saved.getUserEmail())
                .setUserName(saved.getUserName())
                .setUserPicture(saved.getUserPicture() != null ? saved.getUserPicture() : "")
                .setContent(saved.getContent())
                .setCreatedAt(saved.getCreatedAt().format(formatter))
                .setUpdatedAt(saved.getUpdatedAt().format(formatter))
                .build();
            
            CreateCommentResponse response = CreateCommentResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Đã đăng bình luận thành công")
                .setComment(commentMessage)
                .build();
                
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            log.error("Error creating comment: {}", e.getMessage());
            CreateCommentResponse response = CreateCommentResponse.newBuilder()
                .setSuccess(false)
                .setMessage("Lỗi khi tạo bình luận: " + e.getMessage())
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
    
    @Override
    public void updateComment(UpdateCommentRequest request, 
                             StreamObserver<UpdateCommentResponse> responseObserver) {
        log.info("gRPC: Updating comment {}", request.getId());
        
        try {
            Comment comment = commentRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Comment not found"));
                
            comment.setContent(request.getContent());
            comment.setUpdatedAt(LocalDateTime.now());
            
            Comment updated = commentRepository.save(comment);
            
            CommentMessage commentMessage = CommentMessage.newBuilder()
                .setId(updated.getId())
                .setMovieId(updated.getMovieId())
                .setUserEmail(updated.getUserEmail())
                .setUserName(updated.getUserName())
                .setUserPicture(updated.getUserPicture() != null ? updated.getUserPicture() : "")
                .setContent(updated.getContent())
                .setCreatedAt(updated.getCreatedAt().format(formatter))
                .setUpdatedAt(updated.getUpdatedAt().format(formatter))
                .build();
            
            UpdateCommentResponse response = UpdateCommentResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Đã cập nhật bình luận")
                .setComment(commentMessage)
                .build();
                
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            log.error("Error updating comment: {}", e.getMessage());
            UpdateCommentResponse response = UpdateCommentResponse.newBuilder()
                .setSuccess(false)
                .setMessage("Lỗi khi cập nhật: " + e.getMessage())
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
    
    @Override
    public void deleteComment(DeleteCommentRequest request, 
                             StreamObserver<DeleteCommentResponse> responseObserver) {
        log.info("gRPC: Deleting comment {}", request.getId());
        
        try {
            commentRepository.deleteById(request.getId());
            
            DeleteCommentResponse response = DeleteCommentResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Đã xóa bình luận")
                .build();
                
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            log.error("Error deleting comment: {}", e.getMessage());
            DeleteCommentResponse response = DeleteCommentResponse.newBuilder()
                .setSuccess(false)
                .setMessage("Lỗi khi xóa: " + e.getMessage())
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
    
    @Override
    public void checkRateLimit(CheckRateLimitRequest request, 
                               StreamObserver<CheckRateLimitResponse> responseObserver) {
        log.info("gRPC: Checking rate limit for {}", request.getUserEmail());
        
        try {
            boolean allowed = rateLimitService.isAllowed(request.getUserEmail());
            int remaining = rateLimitService.getRemainingRequests(request.getUserEmail());
            long resetTime = rateLimitService.getResetTime(request.getUserEmail());
            
            CheckRateLimitResponse response = CheckRateLimitResponse.newBuilder()
                .setAllowed(allowed)
                .setRemaining(remaining)
                .setResetTime(resetTime)
                .build();
                
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            log.error("Error checking rate limit: {}", e.getMessage());
            responseObserver.onError(e);
        }
    }
}
