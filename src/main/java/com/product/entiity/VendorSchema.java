package com.product.entiity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */

@Entity
@Table(name = "vendor_schema")
@Data
@NoArgsConstructor
public class VendorSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vendorId;

    private String vendorName;

    @ManyToOne
    @JoinColumn(name = "prod_id", nullable = false)
    private ProductSchema product;
}
