package com.product.repository;

import com.product.entiity.Product;
import com.product.entiity.ProductSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */

@Repository
public interface ProductServiceRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String keyword);
}
