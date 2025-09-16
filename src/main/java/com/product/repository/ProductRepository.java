package com.product.repository;

import com.product.entiity.ProductSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductSchema, Integer> {
}
