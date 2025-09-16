package com.product.util;

import com.product.exception.ProductServiceException;
import com.product.dto.ProductRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


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
}
