package com.product.exception;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

