package com.product.util;

import com.product.dto.VendorRequest;
import com.product.exception.ProductServiceException;
import com.product.dto.ProductRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
@Component
public class ProductServiceUtils {
    public void validateProductRequest(ProductRequest request) {
        if (StringUtils.isBlank(request.getProdName())) {
            throw new ProductServiceException("Product name cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (request.getProdRate() == null) {
            throw new ProductServiceException("Product rate cannot be null", HttpStatus.BAD_REQUEST);
        }

        if (request.getProdRate() <= 0) {
            throw new ProductServiceException("Product rate must be greater than zero", HttpStatus.BAD_REQUEST);
        }
    }

    public void validateNoDuplicateVendors(List<VendorRequest> vendors) {
        boolean hasDuplicates = vendors.stream()
                .map(VendorRequest::getVendorName)
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                .values()
                .stream()
                .anyMatch(count -> count > 1);

        if (hasDuplicates) {
            throw new IllegalArgumentException("Duplicate vendors are not allowed");
        }
    }

}
