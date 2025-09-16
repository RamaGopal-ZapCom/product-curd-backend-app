package com.product.dto.response;

import lombok.Data;

import java.util.List;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
@Data
public class ProductResponse {
    private Integer prodId;
    private String prodName;
    private Double prodRate;
    private List<VendorResponse> vendors;
}
