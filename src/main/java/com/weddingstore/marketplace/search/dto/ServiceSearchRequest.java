package com.weddingstore.marketplace.search.dto;

import com.weddingstore.common.search.dto.SearchRequest;
import com.weddingstore.marketplace.service.entity.ServiceLocationType;
import com.weddingstore.marketplace.service.entity.ServiceType;
import com.weddingstore.planning.event.entity.EventType;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ServiceSearchRequest extends SearchRequest {

    private String city;

    private Long categoryId;

    private Long vendorId;

    private ServiceType serviceType;

    private ServiceLocationType locationType;

    private EventType eventType;

    @DecimalMin("0.0")
    private BigDecimal minPrice;

    @DecimalMin("0.0")
    private BigDecimal maxPrice;

    @DecimalMin("0.0")
    private BigDecimal minRating;

    private Boolean featured;
}