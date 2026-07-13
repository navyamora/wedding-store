package com.weddingstore.planning.event.dto;

import com.weddingstore.planning.event.entity.EventStatus;
import com.weddingstore.planning.event.entity.EventType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class EventResponse {

    private Long id;
    private Long userId;
    private EventType eventType;
    private String title;
    private String description;
    private LocalDate eventDate;
    private String city;
    private String venue;
    private BigDecimal budget;
    private Integer guestCount;
    private String theme;
    private String imageUrl;
    private EventStatus status;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}