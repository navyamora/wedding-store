package com.weddingstore.booking.controller;

import com.weddingstore.booking.dto.BookingResponse;
import com.weddingstore.booking.dto.UpdateBookingStatusRequest;
import com.weddingstore.booking.service.BookingService;
import com.weddingstore.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/bookings")
@RequiredArgsConstructor
public class VendorBookingController {

    private final BookingService bookingService;

    @GetMapping
    public ApiResponse<List<BookingResponse>> getBookings(
            Authentication authentication
    ) {
        return ApiResponse.<List<BookingResponse>>builder()
                .success(true)
                .message("Vendor bookings fetched successfully")
                .data(bookingService.getVendorBookings(
                        authentication.getName()
                ))
                .build();
    }

    @GetMapping("/{bookingId}")
    public ApiResponse<BookingResponse> getBooking(
            Authentication authentication,
            @PathVariable Long bookingId
    ) {
        return ApiResponse.<BookingResponse>builder()
                .success(true)
                .message("Booking fetched successfully")
                .data(bookingService.getVendorBooking(
                        authentication.getName(),
                        bookingId
                ))
                .build();
    }

    @PutMapping("/{bookingId}/status")
    public ApiResponse<BookingResponse> updateStatus(
            Authentication authentication,
            @PathVariable Long bookingId,
            @Valid @RequestBody UpdateBookingStatusRequest request
    ) {
        return ApiResponse.<BookingResponse>builder()
                .success(true)
                .message("Booking status updated successfully")
                .data(bookingService.updateVendorStatus(
                        authentication.getName(),
                        bookingId,
                        request
                ))
                .build();
    }
}