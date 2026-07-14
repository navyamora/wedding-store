package com.weddingstore.marketplace.service.controller;

import com.weddingstore.common.response.ApiResponse;
import com.weddingstore.marketplace.service.dto.CreateServiceRequest;
import com.weddingstore.marketplace.service.dto.ServiceResponse;
import com.weddingstore.marketplace.service.dto.UpdateServiceRequest;
import com.weddingstore.marketplace.service.service.ServiceOfferingService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor/services")
public class VendorServiceController {

    private final ServiceOfferingService serviceOfferingService;

    public VendorServiceController(
            ServiceOfferingService serviceOfferingService
    ) {
        this.serviceOfferingService = serviceOfferingService;
    }

    @PostMapping
    public ApiResponse<ServiceResponse> create(
            Authentication authentication,
            @Valid @RequestBody CreateServiceRequest request
    ) {
        return ApiResponse.<ServiceResponse>builder()
                .success(true)
                .message("Service created successfully")
                .data(
                        serviceOfferingService.create(
                                authentication.getName(),
                                request
                        )
                )
                .build();
    }

    @GetMapping
    public ApiResponse<List<ServiceResponse>> getMyServices(
            Authentication authentication
    ) {
        return ApiResponse.<List<ServiceResponse>>builder()
                .success(true)
                .message("Vendor services fetched successfully")
                .data(
                        serviceOfferingService.getMyServices(
                                authentication.getName()
                        )
                )
                .build();
    }

    @GetMapping("/{serviceId}")
    public ApiResponse<ServiceResponse> getMyService(
            Authentication authentication,
            @PathVariable Long serviceId
    ) {
        return ApiResponse.<ServiceResponse>builder()
                .success(true)
                .message("Service fetched successfully")
                .data(
                        serviceOfferingService.getMyService(
                                authentication.getName(),
                                serviceId
                        )
                )
                .build();
    }

    @PutMapping("/{serviceId}")
    public ApiResponse<ServiceResponse> update(
            Authentication authentication,
            @PathVariable Long serviceId,
            @Valid @RequestBody UpdateServiceRequest request
    ) {
        return ApiResponse.<ServiceResponse>builder()
                .success(true)
                .message("Service updated successfully")
                .data(
                        serviceOfferingService.update(
                                authentication.getName(),
                                serviceId,
                                request
                        )
                )
                .build();
    }

    @DeleteMapping("/{serviceId}")
    public ApiResponse<Object> delete(
            Authentication authentication,
            @PathVariable Long serviceId
    ) {
        serviceOfferingService.delete(
                authentication.getName(),
                serviceId
        );

        return ApiResponse.builder()
                .success(true)
                .message("Service deleted successfully")
                .data(null)
                .build();
    }
}