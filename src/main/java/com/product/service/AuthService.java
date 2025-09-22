package com.product.service;

import com.product.dto.AuthRequest;
import com.product.dto.ChangePasswordRequest;
import com.product.dto.ExpiredPasswordResetRequest;
import com.product.dto.ResetPasswordRequest;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
public interface AuthService {
    String register(AuthRequest request);
    String login(AuthRequest request);
    String logout(String authHeader);
    String refreshToken(String authHeader);
    String changePassword(ChangePasswordRequest request);
    String resetPassword(ResetPasswordRequest request);
    String getCurrentUser(String authHeader);

    String resetExpiredPassword(ExpiredPasswordResetRequest request);
}
