package com.product.advice;

import com.product.exception.ResourceNotFoundException;
import com.product.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
@RestControllerAdvice
public class ProductServiceExceptionAdvice {

    // Handle custom ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFound(
            ResourceNotFoundException ex) {
        ApiResponse<?> response = new ApiResponse<>(false,
                null, ex.getMessage(),
                null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(
            MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ApiResponse<?> response = new ApiResponse<>(false,
                null, "Validation failed",
                errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        ApiResponse<?> response = new ApiResponse<>(false,
                null, ex.getMessage(),
                null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
