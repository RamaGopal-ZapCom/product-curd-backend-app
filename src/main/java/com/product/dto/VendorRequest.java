package com.product.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
@Data
public class VendorRequest {

    private Integer vendorId;

    @NotBlank(message = "Vendor name cannot be empty")
    private String vendorName;

}
