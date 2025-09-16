package com.product.service.impl;


import com.product.constants.ProductServiceConstants;
import com.product.entiity.ProductSchema;
import com.product.entiity.VendorSchema;
import com.product.exception.ResourceNotFoundException;
import com.product.mapper.ProductServiceMapper;
import com.product.dto.ProductRequest;
import com.product.dto.VendorRequest;
import com.product.dto.response.ApiResponse;
import com.product.dto.response.ProductResponse;
import com.product.repository.ProductRepository;
import com.product.service.ProductService;
import com.product.util.ProductServiceUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductServiceMapper productServiceMapper;

    private final ProductServiceUtils productServiceUtils;

    public ProductServiceImpl(ProductRepository productRepository, ProductServiceMapper productServiceMapper, ProductServiceUtils productServiceUtils) {
        this.productRepository = productRepository;
        this.productServiceMapper = productServiceMapper;
        this.productServiceUtils = productServiceUtils;
    }

    @Override
    public ApiResponse<ProductResponse> createProduct(ProductRequest productRequest) {
        productServiceUtils.validateProductRequest(productRequest);

        ProductSchema product = productServiceMapper.mapToEntity(productRequest);
        ProductSchema savedProduct = productRepository.save(product);

        return new ApiResponse<>(true,
                productServiceMapper.mapToResponse(savedProduct),
                ProductServiceConstants.PRODUCT_CREATED_SUCCESS, null);
    }

    @Override
    public ApiResponse<ProductResponse> getProductById(Integer id) {
        ProductSchema product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ProductServiceConstants.PRODUCT_NOT_FOUND+ "  "+ id));

        return new ApiResponse<>(true,
                productServiceMapper.mapToResponse(product),
                ProductServiceConstants.PRODUCT_FETCHED_SUCCESS, null);
    }

    @Override
    public ApiResponse<?> getAllProducts() {
        List<ProductSchema> products = productRepository.findAll();

        List<ProductResponse> responses = products.stream()
                .map(productServiceMapper::mapToResponse)
                .collect(Collectors.toList());

        return new ApiResponse<>(true,
                responses,
                ProductServiceConstants.PRODUCTS_FETCHED_SUCCESS, null);
    }

    @Override
    public ApiResponse<ProductResponse> updateProduct(Integer id, ProductRequest productRequest) {
        productServiceUtils.validateProductRequest(productRequest);

        ProductSchema existing = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ProductServiceConstants.PRODUCT_NOT_FOUND+"  " + id));

        existing.setProdName(productRequest.getProdName());
        existing.setProdRate(productRequest.getProdRate());

        // Loop through vendors in request
        for (VendorRequest vReq : productRequest.getVendors()) {
            if (vReq.getVendorId() != null) {
                // Find the matching vendor in existing vendors
                existing.getVendors().stream()
                        .filter(v -> v.getVendorId().equals(vReq.getVendorId()))
                        .findFirst()
                        .ifPresent(vendor -> {
                            vendor.setVendorName(vReq.getVendorName());
                            // update other fields if needed
                        });
            } else {
                // This is a new vendor (no ID), so add it
                VendorSchema newVendor = new VendorSchema();
                newVendor.setVendorName(vReq.getVendorName());
                newVendor.setProduct(existing);
                existing.getVendors().add(newVendor);
            }
        }

        ProductSchema updated = productRepository.save(existing);
        return new ApiResponse<>(
                true,
                productServiceMapper.mapToResponse(updated),
                ProductServiceConstants.PRODUCT_UPDATED_SUCCESS,
                null
        );
    }

    @Override
    public ApiResponse<ProductResponse> partialUpdateProduct(Integer id, ProductRequest productRequest) {
        // Fetch existing product
        ProductSchema existing = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ProductServiceConstants.PRODUCT_NOT_FOUND+ "  " + id));

        // Update product fields if present
        if (StringUtils.isNotBlank(productRequest.getProdName())) {
            existing.setProdName(productRequest.getProdName());
        }
        if (productRequest.getProdRate() != null) {
            existing.setProdRate(productRequest.getProdRate());
        }
        // Initialize vendors list if null
        if (ObjectUtils.isEmpty(existing.getVendors())) {
            existing.setVendors(new ArrayList<>());
        }

        // Update vendors if present
        if (productRequest.getVendors() != null && !productRequest.getVendors().isEmpty()) {
            List<VendorSchema> existingVendors = existing.getVendors();

            for (VendorRequest vReq : productRequest.getVendors()) {
                if (vReq.getVendorId() != null) {
                    // Update existing vendor
                    existingVendors.stream()
                            .filter(v -> v.getVendorId().equals(vReq.getVendorId()))
                            .findFirst()
                            .ifPresent(vendor -> vendor.setVendorName(vReq.getVendorName()));
                } else {
                    // Add new vendor
                    VendorSchema newVendor = new VendorSchema();
                    newVendor.setVendorName(vReq.getVendorName());
                    newVendor.setProduct(existing);
                    existingVendors.add(newVendor);
                }
            }
        }
        // Save updated product
        ProductSchema updated = productRepository.save(existing);

        // Return response
        return new ApiResponse<>(
                true,
                productServiceMapper.mapToResponse(updated),
                ProductServiceConstants.PRODUCT_PARTIALLY_UPDATED_SUCCESS,
                null
        );
    }


    @Override
    public ApiResponse<?> deleteProduct(Integer id) {
        ProductSchema existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.delete(existing);
        return new ApiResponse<>(true,
                null,
                ProductServiceConstants.PRODUCT_DELETED_SUCCESS,
                null);
    }


}
