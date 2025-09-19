package com.product.entiity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
@Entity
@Table(name = "category_schema")
@Data
@NoArgsConstructor
public class CategorySchema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductSchema> products;
}
