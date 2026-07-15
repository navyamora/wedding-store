package com.weddingstore.marketplace.search.dto;

import com.weddingstore.common.search.dto.SearchRequest;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductSearchRequest extends SearchRequest {

    private Long categoryId;

    @DecimalMin("0.0")
    private BigDecimal minPrice;

    @DecimalMin("0.0")
    private BigDecimal maxPrice;

    private Boolean featured;

    private Boolean inStock;
}