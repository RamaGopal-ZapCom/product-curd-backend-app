package com.product.service;

import com.product.dto.ProductRequest;
import com.product.dto.response.ApiResponse;
import com.product.dto.response.ProductResponse;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
public interface ProductService {
    ApiResponse<?> createProduct(ProductRequest productRequest);


    ApiResponse<ProductResponse> getProductById(Integer id);

    ApiResponse<?> getAllProducts();

    ApiResponse<ProductResponse> updateProduct(Integer id, ProductRequest productRequest);

    ApiResponse<ProductResponse> partialUpdateProduct(Integer id, ProductRequest productRequest);

    ApiResponse<?> deleteProduct(Integer id);
}
