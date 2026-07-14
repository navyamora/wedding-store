package com.weddingstore.marketplace.availability.controller;

import com.weddingstore.common.response.ApiResponse;
import com.weddingstore.marketplace.availability.dto.*;
import com.weddingstore.marketplace.availability.service.ServiceAvailabilityService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor/services/{serviceId}/availability")
public class VendorAvailabilityController {

    private final ServiceAvailabilityService availabilityService;

    public VendorAvailabilityController(
            ServiceAvailabilityService availabilityService
    ) {
        this.availabilityService = availabilityService;
    }

    @PostMapping
    public ApiResponse<AvailabilityResponse> create(
            Authentication authentication,
            @PathVariable Long serviceId,
            @Valid @RequestBody CreateAvailabilityRequest request
    ) {
        return ApiResponse.<AvailabilityResponse>builder()
                .success(true)
                .message("Availability created successfully")
                .data(
                        availabilityService.createAvailability(
                                authentication.getName(),
                                serviceId,
                                request
                        )
                )
                .build();
    }

    @PutMapping("/{availabilityId}")
    public ApiResponse<AvailabilityResponse> update(
            Authentication authentication,
            @PathVariable Long serviceId,
            @PathVariable Long availabilityId,
            @Valid @RequestBody UpdateAvailabilityRequest request
    ) {
        return ApiResponse.<AvailabilityResponse>builder()
                .success(true)
                .message("Availability updated successfully")
                .data(
                        availabilityService.updateAvailability(
                                authentication.getName(),
                                serviceId,
                                availabilityId,
                                request
                        )
                )
                .build();
    }

    @DeleteMapping("/{availabilityId}")
    public ApiResponse<Object> delete(
            Authentication authentication,
            @PathVariable Long serviceId,
            @PathVariable Long availabilityId
    ) {
        availabilityService.deleteAvailability(
                authentication.getName(),
                serviceId,
                availabilityId
        );

        return ApiResponse.builder()
                .success(true)
                .message("Availability deleted successfully")
                .data(null)
                .build();
    }

    @PostMapping("/blocked-dates")
    public ApiResponse<BlockedDateResponse> addBlockedDate(
            Authentication authentication,
            @PathVariable Long serviceId,
            @Valid @RequestBody CreateBlockedDateRequest request
    ) {
        return ApiResponse.<BlockedDateResponse>builder()
                .success(true)
                .message("Date blocked successfully")
                .data(
                        availabilityService.addBlockedDate(
                                authentication.getName(),
                                serviceId,
                                request
                        )
                )
                .build();
    }

    @DeleteMapping("/blocked-dates/{blockedDateId}")
    public ApiResponse<Object> deleteBlockedDate(
            Authentication authentication,
            @PathVariable Long serviceId,
            @PathVariable Long blockedDateId
    ) {
        availabilityService.deleteBlockedDate(
                authentication.getName(),
                serviceId,
                blockedDateId
        );

        return ApiResponse.builder()
                .success(true)
                .message("Blocked date removed successfully")
                .data(null)
                .build();
    }
}