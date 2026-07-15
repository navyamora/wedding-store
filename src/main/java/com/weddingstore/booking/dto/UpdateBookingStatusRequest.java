package com.weddingstore.booking.dto;

import com.weddingstore.booking.entity.BookingStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateBookingStatusRequest {

    @NotNull
    private BookingStatus status;

    @Size(max = 5000)
    private String vendorMessage;
}