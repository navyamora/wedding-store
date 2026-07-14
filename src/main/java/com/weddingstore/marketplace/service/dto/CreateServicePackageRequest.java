package com.weddingstore.marketplace.service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateServicePackageRequest {

    @NotBlank
    @Size(max = 120)
    private String packageName;

    @Size(max = 5000)
    private String description;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @Min(1)
    private Integer durationMinutes;

    @Size(max = 5000)
    private String inclusions;

    @Size(max = 5000)
    private String exclusions;

    private Integer displayOrder;
}