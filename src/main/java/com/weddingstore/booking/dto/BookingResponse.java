package com.weddingstore.booking.dto;

import com.weddingstore.booking.entity.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class BookingResponse {

    private Long id;

    private Long customerId;
    private String customerName;
    private String customerEmail;

    private Long eventId;
    private String eventTitle;
    private String eventType;

    private Long serviceId;
    private String serviceName;

    private Long vendorId;
    private String vendorBusinessName;

    private Long packageId;
    private String packageName;

    private LocalDate requestedDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private String location;
    private String customerNotes;
    private String vendorMessage;

    private BigDecimal estimatedAmount;
    private BookingStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}