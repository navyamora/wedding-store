package com.weddingstore.marketplace.availability.controller;

import com.weddingstore.common.response.ApiResponse;
import com.weddingstore.marketplace.availability.dto.AvailabilityResponse;
import com.weddingstore.marketplace.availability.dto.BlockedDateResponse;
import com.weddingstore.marketplace.availability.service.ServiceAvailabilityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services/{serviceId}/availability")
public class PublicAvailabilityController {

    private final ServiceAvailabilityService availabilityService;

    public PublicAvailabilityController(
            ServiceAvailabilityService availabilityService
    ) {
        this.availabilityService = availabilityService;
    }

    @GetMapping
    public ApiResponse<List<AvailabilityResponse>> getAvailability(
            @PathVariable Long serviceId
    ) {
        return ApiResponse.<List<AvailabilityResponse>>builder()
                .success(true)
                .message("Availability fetched successfully")
                .data(
                        availabilityService.getAvailability(
                                serviceId
                        )
                )
                .build();
    }

    @GetMapping("/blocked-dates")
    public ApiResponse<List<BlockedDateResponse>> getBlockedDates(
            @PathVariable Long serviceId
    ) {
        return ApiResponse.<List<BlockedDateResponse>>builder()
                .success(true)
                .message("Blocked dates fetched successfully")
                .data(
                        availabilityService.getBlockedDates(
                                serviceId
                        )
                )
                .build();
    }
}