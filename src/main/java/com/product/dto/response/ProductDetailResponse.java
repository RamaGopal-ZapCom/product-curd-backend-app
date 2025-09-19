package com.product.dto.response;

import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class ProductDetailResponse {
    private Integer prodId;
    private String prodName;
    private Double prodRate;

    private Set<String> vendorNames; // nested Set
    private List<String> reviews;    // nested List
    private Map<String, Double> vendorPriceMap; // vendor → price

    private List<Map<String, Object>> attributes; // List of Map
    private Map<String, Map<String, String>> metadata; // Map of Map
}
