package com.product.controller;

import com.product.dto.ProductRequest;
import com.product.dto.response.ApiResponse;
import com.product.dto.response.ProductResponse;
import com.product.entiity.Product;
import com.product.repository.ProductRepository;
import com.product.repository.ProductServiceRepository;
import com.product.service.ProductService;
import com.product.service.impl.ProductAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */

@RestController
@RequestMapping("/api/{application-name}")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductServiceRepository repository;
    @Autowired
    private ProductAnalyticsService analyticsService;

    @PostMapping("/createProduct")
    @Validated
    public ResponseEntity<ApiResponse<?>> createProduct(
           @Valid @RequestBody ProductRequest productRequest) {
        ApiResponse<?> response = productService.createProduct(productRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/getProductById/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Integer id) {
        ApiResponse<ProductResponse> response = productService.getProductById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<ApiResponse<?>> getAllProducts() {
        ApiResponse<?> response = productService.getAllProducts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ApiResponse<?>> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductRequest productRequest) {
        ApiResponse<?> response = productService.updateProduct(id, productRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/partialUpdateProduct/{id}")
    public ResponseEntity<ApiResponse<?>> partialUpdateProduct(
            @PathVariable Integer id,
            @RequestBody ProductRequest productRequest) {
        ApiResponse<?> response = productService.partialUpdateProduct(id, productRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable Integer id) {
        ApiResponse<?> response = productService.deleteProduct(id);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }


    // ================= Create Product =================
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        // Check for duplicate SKU using Java 8
        boolean duplicateSku = repository.findAll().stream()
                .anyMatch(
                        p -> p.getSku().equalsIgnoreCase(product.getSku())
                );
        if (duplicateSku) {
            throw new RuntimeException("Duplicate SKU found!");
        }
        return repository.save(product);
    }

    // ================= Read All Products =================
    @GetMapping
    public List<Product> getAllProductss() {
        return repository.findAll();
    }

    // ================= Get Products Sorted by Price (Desc) =================
    @GetMapping("/sorted/price")
    public List<Product> getProductsSortedByPriceDesc() {
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .collect(Collectors.toList());
    }

    // ================= Top 3 Rated Products =================
    @GetMapping("/top-rated")
    public List<Product> getTopRatedProducts() {
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(Product::getRating).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    // ================= Products Filtered by Stock =================
    @GetMapping("/in-stock")
    public List<Product> getAvailableProducts() {
        return repository.findAll().stream()
                .filter(p -> p.getStock() > 0)
                .collect(Collectors.toList());
    }

    // ================= Most Expensive and Cheapest Products =================
    @GetMapping("/price-extremes")
    public Map<String, Product> getPriceExtremes() {
        List<Product> products = repository.findAll();
        Product max = products.stream()
                .max(Comparator.comparing(Product::getPrice))
                .orElse(null);
        Product min = products.stream()
                .min(Comparator.comparing(Product::getPrice))
                .orElse(null);
        Map<String, Product> map = new HashMap<>();
        map.put("mostExpensive", max);
        map.put("cheapest", min);
        return map;
    }

    // ================= Find Duplicate Product Names =================
    @GetMapping("/duplicates")
    public Set<String> getDuplicateProductNames() {
        Set<String> unique = new HashSet<>();
        return repository.findAll()
                .stream()
                .map(Product::getName)
                .filter(name -> !unique.add(name))
                .collect(Collectors.toSet());
    }

    // ================= Get All Tags as Single String =================
    @GetMapping("/tags")
    public String getAllTags() {
        return repository.findAll()
                .stream()
                .flatMap(p -> p.getTags().stream())
                .distinct()
                .collect(Collectors.joining(", ", "[", "]"));
    }

    // ================= Average Rating of Products =================
    @GetMapping("/average-rating")
    public double getAverageRating() {
        return repository.findAll().stream()
                .mapToDouble(Product::getRating)
                .average()
                .orElse(0);
    }

    // ================= Search Products by Name Containing Keyword =================
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String keyword) {
        return repository.findByNameContaining(keyword);
    }

    // ================= Delete Product =================
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        repository.deleteById(id);
        return "Product deleted successfully!";
    }

    @GetMapping("/analytics/avg-price-category")
    public Map<String, Double> getAvgPriceByCategory() {
        return analyticsService.averagePriceByCategory();
    }

    @GetMapping("/analytics/category-wise-products")
    public Map<String, Map<Integer, String>> getCategoryWiseProducts() {
        return analyticsService.categoryWiseProductMap();
    }

    @GetMapping("/analytics/product-attributes")
    public List<Map<String, Object>> getProductAttributes() {
        return analyticsService.buildProductAttributes();
    }

}
