package com.weddingstore.marketplace.availability.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BlockedDateResponse {

    private Long id;
    private Long serviceId;
    private LocalDate blockedDate;
    private String reason;
    private Boolean active;
}