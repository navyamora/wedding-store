package com.weddingstore.marketplace.service.controller;

import com.weddingstore.common.response.ApiResponse;
import com.weddingstore.marketplace.service.dto.ServiceResponse;
import com.weddingstore.marketplace.service.entity.ServiceType;
import com.weddingstore.marketplace.service.service.ServiceOfferingService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/services")
public class PublicServiceController {

    private final ServiceOfferingService serviceOfferingService;

    public PublicServiceController(
            ServiceOfferingService serviceOfferingService
    ) {
        this.serviceOfferingService = serviceOfferingService;
    }

    @GetMapping
    public ApiResponse<List<ServiceResponse>> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) ServiceType serviceType,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean featured
    ) {
        return ApiResponse.<List<ServiceResponse>>builder()
                .success(true)
                .message("Services fetched successfully")
                .data(
                        serviceOfferingService.searchPublic(
                                city,
                                serviceType,
                                categoryId,
                                maxPrice,
                                featured
                        )
                )
                .build();
    }

    @GetMapping("/{serviceId}")
    public ApiResponse<ServiceResponse> getById(
            @PathVariable Long serviceId
    ) {
        return ApiResponse.<ServiceResponse>builder()
                .success(true)
                .message("Service fetched successfully")
                .data(
                        serviceOfferingService.getPublicById(
                                serviceId
                        )
                )
                .build();
    }
}