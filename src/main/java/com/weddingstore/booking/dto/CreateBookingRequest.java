package com.weddingstore.booking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateBookingRequest {

    @NotNull
    private Long eventId;

    @NotNull
    private Long serviceId;

    private Long packageId;

    @NotNull
    @FutureOrPresent
    private LocalDate requestedDate;

    @NotNull
    private LocalTime startTime;

    @NotBlank
    @Size(max = 500)
    private String location;

    @Size(max = 5000)
    private String customerNotes;
}