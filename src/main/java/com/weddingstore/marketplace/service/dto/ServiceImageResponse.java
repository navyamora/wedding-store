package com.weddingstore.marketplace.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceImageResponse {

    private Long id;
    private String imageUrl;
    private Integer displayOrder;
    private Boolean primaryImage;
    private String altText;
}