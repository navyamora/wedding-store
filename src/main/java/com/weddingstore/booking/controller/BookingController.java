package com.weddingstore.booking.controller;

import com.weddingstore.booking.dto.BookingResponse;
import com.weddingstore.booking.dto.CreateBookingRequest;
import com.weddingstore.booking.service.BookingService;
import com.weddingstore.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ApiResponse<BookingResponse> create(
            Authentication authentication,
            @Valid @RequestBody CreateBookingRequest request
    ) {
        return ApiResponse.<BookingResponse>builder()
                .success(true)
                .message("Booking request created successfully")
                .data(bookingService.create(
                        authentication.getName(),
                        request
                ))
                .build();
    }

    @GetMapping("/my")
    public ApiResponse<List<BookingResponse>> getMyBookings(
            Authentication authentication
    ) {
        return ApiResponse.<List<BookingResponse>>builder()
                .success(true)
                .message("Bookings fetched successfully")
                .data(bookingService.getMyBookings(
                        authentication.getName()
                ))
                .build();
    }

    @PutMapping("/{bookingId}/cancel")
    public ApiResponse<BookingResponse> cancel(
            Authentication authentication,
            @PathVariable Long bookingId
    ) {
        return ApiResponse.<BookingResponse>builder()
                .success(true)
                .message("Booking cancelled successfully")
                .data(bookingService.cancel(
                        authentication.getName(),
                        bookingId
                ))
                .build();
    }
}