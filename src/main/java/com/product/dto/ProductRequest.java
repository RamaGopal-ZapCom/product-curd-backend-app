package com.product.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
@Data
public class ProductRequest {

    @NotBlank(message = "Product name cannot be empty")
    private String prodName;

    @NotNull(message = "Product rate cannot be null")
    private Double prodRate;

    private List<VendorRequest> vendors;
}
