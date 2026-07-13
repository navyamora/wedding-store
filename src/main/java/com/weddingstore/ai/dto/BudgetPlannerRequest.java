package com.weddingstore.ai.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetPlannerRequest {

    @NotBlank
    private String city;

    @NotNull
    @DecimalMin(value = "1.00")
    private BigDecimal budget;

    @NotNull
    @Min(1)
    private Integer guests;

    private String weddingType;

    private String preferences;
}