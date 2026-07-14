package com.weddingstore.marketplace.service.dto;

import com.weddingstore.marketplace.service.entity.ServiceLocationType;
import com.weddingstore.marketplace.service.entity.ServiceType;
import com.weddingstore.planning.event.entity.EventType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class ServiceResponse {

    private Long id;

    private Long vendorId;
    private String vendorBusinessName;

    private Long categoryId;
    private String categoryName;

    private ServiceType serviceType;

    private String name;
    private String slug;
    private String shortDescription;
    private String description;

    private BigDecimal basePrice;
    private Integer durationMinutes;

    private ServiceLocationType locationType;

    private String city;
    private String state;
    private String country;

    private Integer serviceRadiusKm;
    private BigDecimal advancePercentage;

    private Boolean featured;
    private Boolean active;
    private BigDecimal rating;

    private Set<EventType> applicableEventTypes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}