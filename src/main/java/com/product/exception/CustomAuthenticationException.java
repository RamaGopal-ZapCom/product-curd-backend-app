package com.product.exception;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
public class CustomAuthenticationException extends RuntimeException {
    public CustomAuthenticationException(String message) {
        super(message);
    }
}
