package com.product.service.impl;

import com.product.constants.UserRole;
import com.product.dto.AuthRequest;
import com.product.dto.ChangePasswordRequest;
import com.product.dto.ResetPasswordRequest;
import com.product.entiity.AppUser;
import com.product.repository.UserRepository;
import com.product.service.AuthService;
import com.product.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // token -> username
    private Map<String, String> resetTokens = new HashMap<>();

    private Set<String> tokenBlacklist = new HashSet<>();

    @Override
    public String register(AuthRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.ROLE_USER);
        userRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public String login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username/password");
        }
        String role = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getRole();
        return jwtUtil.generateToken(request.getUsername(), role);
    }

    @Override
    public String logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenBlacklist.add(authHeader.substring(7));
        }
        return "Logged out successfully";
    }

    @Override
    public String refreshToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String oldToken = authHeader.substring(7);
            if (!tokenBlacklist.contains(oldToken)) {
                return jwtUtil.refreshToken(oldToken);
            }
        }
        throw new RuntimeException("Invalid or expired token");
    }

    @Override
    public String changePassword(ChangePasswordRequest request) {
        // 1. Get current authenticated user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Validate old password
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // 3. Update with new password
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        return "Password changed successfully";
    }


    @Override
    public String resetPassword(ResetPasswordRequest request) {
        // 1. Verify token
        String username = resetTokens.get(request.token());
        if (username == null || !username.equals(request.email())) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        // 2. Find user
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Update password
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        // 4. Remove token after use
        resetTokens.remove(request.token());

        return "Password reset successfully";
    }


    @Override
    public String getCurrentUser(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUsername(token);
        }
        throw new RuntimeException("Invalid token");
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}