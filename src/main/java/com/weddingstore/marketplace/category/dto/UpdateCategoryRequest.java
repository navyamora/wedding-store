package com.weddingstore.marketplace.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCategoryRequest {

    @NotBlank
    private String name;

    private String description;

    private String imageUrl;

    private Integer displayOrder;

    private Boolean active;
}