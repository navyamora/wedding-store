package com.weddingstore.marketplace.service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ServicePackageResponse {

    private Long id;
    private String packageName;
    private String description;
    private BigDecimal price;
    private Integer durationMinutes;
    private String inclusions;
    private String exclusions;
    private Boolean active;
    private Integer displayOrder;
}