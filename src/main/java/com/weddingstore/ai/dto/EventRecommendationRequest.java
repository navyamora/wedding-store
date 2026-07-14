package com.weddingstore.ai.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EventRecommendationRequest {

    @Size(max = 1000)
    private String preferences;

    private Long categoryId;

    private BigDecimal maxPrice;
}