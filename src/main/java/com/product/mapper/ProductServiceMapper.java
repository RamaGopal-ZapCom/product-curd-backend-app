package com.product.mapper;

import com.product.entiity.ProductSchema;
import com.product.entiity.VendorSchema;
import com.product.dto.ProductRequest;
import com.product.dto.response.ProductResponse;
import com.product.dto.response.VendorResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */

@Component
public class ProductServiceMapper {

    public ProductSchema mapToEntity(ProductRequest request) {
        ProductSchema product = new ProductSchema();
        product.setProdName(request.getProdName());
        product.setProdRate(request.getProdRate());

        List<VendorSchema> vendors = request.getVendors()
                .stream()
                .map(v -> {
                    VendorSchema vendor = new VendorSchema();
                    vendor.setVendorName(v.getVendorName());
                    vendor.setProduct(product);
                    return vendor;
                }).collect(Collectors.toList());

        product.setVendors(vendors);
        return product;
    }

    public ProductResponse mapToResponse(ProductSchema product) {
        ProductResponse response = new ProductResponse();
        response.setProdId(product.getProdId());
        response.setProdName(product.getProdName());
        response.setProdRate(product.getProdRate());

        List<VendorResponse> vendorResponses = product.getVendors()
                .stream()
                .map(v -> {
                    VendorResponse vr = new VendorResponse();
                    vr.setVendorId(v.getVendorId());
                    vr.setVendorName(v.getVendorName());
                    return vr;
                }).collect(Collectors.toList());
        response.setVendors(vendorResponses);
        return response;
    }
}
