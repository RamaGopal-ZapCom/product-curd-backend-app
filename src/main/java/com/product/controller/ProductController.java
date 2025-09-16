package com.product.controller;

import com.product.dto.ProductRequest;
import com.product.dto.response.ApiResponse;
import com.product.dto.response.ProductResponse;
import com.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */

@RestController
@RequestMapping("/api/{application-name}")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/createProduct")
    @Validated
    public ResponseEntity<ApiResponse<?>> createProduct(
           @Valid @RequestBody ProductRequest productRequest) {
        ApiResponse<?> response = productService.createProduct(productRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/getProductById/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Integer id) {
        ApiResponse<ProductResponse> response = productService.getProductById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<ApiResponse<?>> getAllProducts() {
        ApiResponse<?> response = productService.getAllProducts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ApiResponse<?>> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductRequest productRequest) {
        ApiResponse<?> response = productService.updateProduct(id, productRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/partialUpdateProduct/{id}")
    public ResponseEntity<ApiResponse<?>> partialUpdateProduct(
            @PathVariable Integer id,
            @RequestBody ProductRequest productRequest) {
        ApiResponse<?> response = productService.partialUpdateProduct(id, productRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable Integer id) {
        ApiResponse<?> response = productService.deleteProduct(id);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
