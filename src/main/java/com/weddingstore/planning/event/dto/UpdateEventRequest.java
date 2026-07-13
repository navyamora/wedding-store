package com.weddingstore.planning.event.dto;

import com.weddingstore.planning.event.entity.EventStatus;
import com.weddingstore.planning.event.entity.EventType;
import jakarta.validation.constraints.*;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateEventRequest {

    @NotNull
    private EventType eventType;

    @NotBlank
    @Size(max = 150)
    private String title;

    @Size(max = 5000)
    private String description;

    @NotNull
    @FutureOrPresent
    private LocalDate eventDate;

    @NotBlank
    @Size(max = 100)
    private String city;

    @Size(max = 250)
    private String venue;

    @DecimalMin(value = "0.0")
    private BigDecimal budget;

    @Min(1)
    private Integer guestCount;

    @Size(max = 150)
    private String theme;

    @Size(max = 1000)
    private String imageUrl;

    @NotNull
    private EventStatus status;
}