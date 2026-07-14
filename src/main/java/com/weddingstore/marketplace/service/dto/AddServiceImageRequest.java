package com.weddingstore.marketplace.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddServiceImageRequest {

    @NotBlank
    @Size(max = 1000)
    private String imageUrl;

    private Integer displayOrder;

    private Boolean primaryImage;

    @Size(max = 250)
    private String altText;
}