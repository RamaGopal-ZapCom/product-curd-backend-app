package com.product.controller;

import com.product.dto.AuthRequest;
import com.product.dto.ChangePasswordRequest;
import com.product.dto.ResetPasswordRequest;
import com.product.dto.response.ApiResponse;
import com.product.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody AuthRequest request) {
        String message = authService.register(request);
        return ResponseEntity.ok(new ApiResponse<>(true, null, message, null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody AuthRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new ApiResponse<>(true, token, "Login successful", null));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String authHeader) {
        String message = authService.logout(authHeader);
        return ResponseEntity.ok(new ApiResponse<>(true, null, message, null));
    }

    //TODO Instead of forcing the user to login again,
    // a refreshToken endpoint allows them to get a new valid token without re-entering credentials.
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refreshToken(@RequestHeader("Authorization") String authHeader) {
        String newToken = authService.refreshToken(authHeader);
        return ResponseEntity.ok(new ApiResponse<>(true, newToken, "Token refreshed successfully", null));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody ChangePasswordRequest request) {
        String message = authService.changePassword(request);
        return ResponseEntity.ok(new ApiResponse<>(true, null, message, null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        String message = authService.resetPassword(request);
        return ResponseEntity.ok(new ApiResponse<>(true, null, message, null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<String>> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String username = authService.getCurrentUser(authHeader);
        return ResponseEntity.ok(new ApiResponse<>(true, username, "User info fetched", null));
    }
}

