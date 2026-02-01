package com.movie.auth.grpc;

import com.movie.auth.security.JwtUtil;
import com.movie.auth.service.JwtBlacklistService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {
    
    private final JwtUtil jwtUtil;
    private final JwtBlacklistService jwtBlacklistService;
    
    @Override
    public void verifyToken(VerifyTokenRequest request, StreamObserver<VerifyTokenResponse> responseObserver) {
        log.info("gRPC: Verifying token");
        
        try {
            String token = request.getToken();
            
            // Check if blacklisted
            if (jwtBlacklistService.isBlacklisted(token)) {
                VerifyTokenResponse response = VerifyTokenResponse.newBuilder()
                    .setValid(false)
                    .setMessage("Token đã bị vô hiệu hóa")
                    .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
            
            // Check if expired
            if (jwtUtil.isTokenExpired(token)) {
                VerifyTokenResponse response = VerifyTokenResponse.newBuilder()
                    .setValid(false)
                    .setMessage("Token đã hết hạn")
                    .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
            
            // Get user info from token
            String email = jwtUtil.getEmailFromToken(token);
            
            VerifyTokenResponse response = VerifyTokenResponse.newBuilder()
                .setValid(true)
                .setEmail(email)
                .setMessage("Token hợp lệ")
                .build();
                
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            log.error("Error verifying token: {}", e.getMessage());
            VerifyTokenResponse response = VerifyTokenResponse.newBuilder()
                .setValid(false)
                .setMessage("Lỗi xác thực token: " + e.getMessage())
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
    
    @Override
    public void logout(LogoutRequest request, StreamObserver<LogoutResponse> responseObserver) {
        log.info("gRPC: Logout request");
        
        try {
            String token = request.getToken();
            long secondsUntilExpiration = jwtUtil.getSecondsUntilExpiration(token);
            
            if (secondsUntilExpiration > 0) {
                jwtBlacklistService.blacklistToken(token, secondsUntilExpiration);
            }
            
            LogoutResponse response = LogoutResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Đăng xuất thành công")
                .build();
                
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage());
            LogoutResponse response = LogoutResponse.newBuilder()
                .setSuccess(false)
                .setMessage("Lỗi khi đăng xuất: " + e.getMessage())
                .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
    
    @Override
    public void isTokenBlacklisted(IsTokenBlacklistedRequest request, StreamObserver<IsTokenBlacklistedResponse> responseObserver) {
        log.info("gRPC: Checking if token is blacklisted");
        
        try {
            String token = request.getToken();
            boolean blacklisted = jwtBlacklistService.isBlacklisted(token);
            
            IsTokenBlacklistedResponse response = IsTokenBlacklistedResponse.newBuilder()
                .setBlacklisted(blacklisted)
                .build();
                
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            log.error("Error checking blacklist: {}", e.getMessage());
            responseObserver.onError(e);
        }
    }
}
