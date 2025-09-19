package com.product.service.impl;


import com.product.entiity.ProductSchema;
import com.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductAnalyticsService {

    private final ProductRepository productRepository;

    public ProductAnalyticsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 1. Group products by category name
    public Map<String, List<ProductSchema>> groupByCategory() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(p -> p.getCategory().getCategoryName()));
    }

    // 2. Map productId → Product Name
    public Map<Integer, String> mapProductIdToName() {
        return productRepository.findAll().stream()
                .collect(Collectors.toMap(ProductSchema::getProdId, ProductSchema::getProdName));
    }

    // 3. Map Category → Average Price
    public Map<String, Double> averagePriceByCategory() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCategory().getCategoryName(),
                        Collectors.averagingDouble(ProductSchema::getProdRate)
                ));
    }

    // 4. Find duplicate product rates
    public Set<Double> findDuplicateRates() {
        Set<Double> seen = new HashSet<>();
        return productRepository.findAll().stream()
                .map(ProductSchema::getProdRate)
                .filter(rate -> !seen.add(rate))
                .collect(Collectors.toSet());
    }

    // 5. Build nested collection response
    public List<Map<String, Object>> buildProductAttributes() {
        return productRepository.findAll().stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", p.getProdId());
                    map.put("name", p.getProdName());
                    map.put("tags", Arrays.asList("new", "discount")); // example List inside Map
                    return map;
                })
                .collect(Collectors.toList());
    }

    // 6. Map of Map Example: category → (productId → productName)
    public Map<String, Map<Integer, String>> categoryWiseProductMap() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCategory().getCategoryName(),
                        Collectors.toMap(ProductSchema::getProdId, ProductSchema::getProdName)
                ));
    }

    // 7. Find top rated product per category
    public Map<String, Optional<ProductSchema>> topRatedProductPerCategory() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCategory().getCategoryName(),
                        Collectors.maxBy(Comparator.comparingDouble(p -> p.getReviews().stream()
                                .mapToInt(r -> r.getRating()).average().orElse(0)))
                ));
    }
}

