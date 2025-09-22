package com.product.dto;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
public record ResetPasswordRequest (String email, String token, String newPassword){

}
