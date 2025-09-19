package com.product.entiity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */

@Entity
@Table(name = "product_schema")
@Data
@NoArgsConstructor
public class ProductSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prodId;

    private String prodName;
    private Double prodRate;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendorSchema> vendors;

    private Double rating;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategorySchema category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewSchema> reviews;
}
