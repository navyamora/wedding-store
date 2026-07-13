package com.weddingstore.marketplace.product.entity;

import com.weddingstore.marketplace.category.entity.Category;
import com.weddingstore.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=150)
    private String name;

    @Column(nullable=false, unique=true, length=180)
    private String slug;

    @Column(length=500)
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(unique=true, length=100)
    private String sku;

    @Column(nullable=false)
    private BigDecimal price;

    private BigDecimal discountPrice;

    private Integer quantity;

    @Builder.Default
    private Boolean featured = false;

    @Builder.Default
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable=false)
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();
}