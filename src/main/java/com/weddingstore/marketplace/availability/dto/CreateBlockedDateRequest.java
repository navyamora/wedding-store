package com.weddingstore.marketplace.availability.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateBlockedDateRequest {

    @NotNull
    @FutureOrPresent
    private LocalDate blockedDate;

    @Size(max = 500)
    private String reason;
}