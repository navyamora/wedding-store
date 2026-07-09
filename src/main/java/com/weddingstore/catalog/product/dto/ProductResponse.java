package com.weddingstore.catalog.product.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String slug;
    private String shortDescription;
    private String description;
    private String sku;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Integer quantity;
    private Boolean featured;
    private Boolean active;
    private Long categoryId;
    private String categoryName;
    private List<ProductImageResponse> images;
}