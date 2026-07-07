package com.weddingstore.catalog.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequest {

    @NotBlank
    private String name;

    private String description;

    private String imageUrl;
    
    private Integer displayOrder;
}