package com.weddingstore.catalog.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateProductRequest {

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

    private Boolean active;

    @NotNull
    private Long categoryId;

    private List<ProductImageRequest> images;
}