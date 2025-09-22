package com.product.dto;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
public record ChangePasswordRequest(String oldPassword, String newPassword) {
}
