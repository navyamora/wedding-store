package com.weddingstore.marketplace.service.controller;

import com.weddingstore.common.response.ApiResponse;
import com.weddingstore.marketplace.service.dto.ServiceImageResponse;
import com.weddingstore.marketplace.service.dto.ServicePackageResponse;
import com.weddingstore.marketplace.service.service.ServiceContentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services/{serviceId}")
public class PublicServiceContentController {

    private final ServiceContentService contentService;

    public PublicServiceContentController(
            ServiceContentService contentService
    ) {
        this.contentService = contentService;
    }

    @GetMapping("/images")
    public ApiResponse<List<ServiceImageResponse>> getImages(
            @PathVariable Long serviceId
    ) {
        return ApiResponse.<List<ServiceImageResponse>>builder()
                .success(true)
                .message("Service images fetched successfully")
                .data(contentService.getImages(serviceId))
                .build();
    }

    @GetMapping("/packages")
    public ApiResponse<List<ServicePackageResponse>> getPackages(
            @PathVariable Long serviceId
    ) {
        return ApiResponse.<List<ServicePackageResponse>>builder()
                .success(true)
                .message("Service packages fetched successfully")
                .data(contentService.getPackages(serviceId))
                .build();
    }
}