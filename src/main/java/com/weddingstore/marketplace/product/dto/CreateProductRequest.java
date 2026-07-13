package com.weddingstore.marketplace.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateProductRequest {

    @NotBlank
    private String name;

    private String shortDescription;

    private String description;

    @NotBlank
    private String sku;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    private BigDecimal discountPrice;

    private Integer quantity;

    private Boolean featured;

    @NotNull
    private Long categoryId;

    private List<ProductImageRequest> images;
}