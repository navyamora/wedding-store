package com.weddingstore.marketplace.service.dto;

import com.weddingstore.marketplace.service.entity.ServiceLocationType;
import com.weddingstore.marketplace.service.entity.ServiceType;
import com.weddingstore.planning.event.entity.EventType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class UpdateServiceRequest {

    @NotNull
    private Long categoryId;

    @NotNull
    private ServiceType serviceType;

    @NotBlank
    @Size(max = 180)
    private String name;

    @Size(max = 500)
    private String shortDescription;

    @Size(max = 5000)
    private String description;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal basePrice;

    @Min(1)
    private Integer durationMinutes;

    @NotNull
    private ServiceLocationType locationType;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 100)
    private String state;

    @Size(max = 100)
    private String country;

    @Min(0)
    private Integer serviceRadiusKm;

    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private BigDecimal advancePercentage;

    private Boolean featured;

    private Boolean active;

    private Set<EventType> applicableEventTypes;
}