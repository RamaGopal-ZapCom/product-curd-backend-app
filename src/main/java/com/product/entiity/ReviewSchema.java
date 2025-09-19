package com.product.entiity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Customer reviews on a product
 */
@Entity
@Table(name = "review_schema")
@Data
@NoArgsConstructor
public class ReviewSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    private String reviewerName;
    private int rating;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "prod_id", nullable = false)
    private ProductSchema product;
}
