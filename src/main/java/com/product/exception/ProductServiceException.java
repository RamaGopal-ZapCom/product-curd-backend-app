package com.product.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
public class ProductServiceException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ProductServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
