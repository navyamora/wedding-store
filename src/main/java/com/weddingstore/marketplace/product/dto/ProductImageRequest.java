package com.weddingstore.marketplace.product.dto;

import lombok.Data;

@Data
public class ProductImageRequest {
    private String imageUrl;
    private Integer displayOrder;
    private Boolean primaryImage;
}