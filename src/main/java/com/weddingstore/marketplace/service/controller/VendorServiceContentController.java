package com.weddingstore.marketplace.service.controller;

import com.weddingstore.common.response.ApiResponse;
import com.weddingstore.marketplace.service.dto.*;
import com.weddingstore.marketplace.service.service.ServiceContentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor/services/{serviceId}")
public class VendorServiceContentController {

    private final ServiceContentService contentService;

    public VendorServiceContentController(
            ServiceContentService contentService
    ) {
        this.contentService = contentService;
    }

    @PostMapping("/images")
    public ApiResponse<ServiceImageResponse> addImage(
            Authentication authentication,
            @PathVariable Long serviceId,
            @Valid @RequestBody AddServiceImageRequest request
    ) {
        return ApiResponse.<ServiceImageResponse>builder()
                .success(true)
                .message("Service image added successfully")
                .data(contentService.addImage(
                        authentication.getName(),
                        serviceId,
                        request
                ))
                .build();
    }

    @DeleteMapping("/images/{imageId}")
    public ApiResponse<Object> deleteImage(
            Authentication authentication,
            @PathVariable Long serviceId,
            @PathVariable Long imageId
    ) {
        contentService.deleteImage(
                authentication.getName(),
                serviceId,
                imageId
        );

        return ApiResponse.builder()
                .success(true)
                .message("Service image deleted successfully")
                .data(null)
                .build();
    }

    @PostMapping("/packages")
    public ApiResponse<ServicePackageResponse> createPackage(
            Authentication authentication,
            @PathVariable Long serviceId,
            @Valid @RequestBody CreateServicePackageRequest request
    ) {
        return ApiResponse.<ServicePackageResponse>builder()
                .success(true)
                .message("Service package created successfully")
                .data(contentService.createPackage(
                        authentication.getName(),
                        serviceId,
                        request
                ))
                .build();
    }

    @PutMapping("/packages/{packageId}")
    public ApiResponse<ServicePackageResponse> updatePackage(
            Authentication authentication,
            @PathVariable Long serviceId,
            @PathVariable Long packageId,
            @Valid @RequestBody UpdateServicePackageRequest request
    ) {
        return ApiResponse.<ServicePackageResponse>builder()
                .success(true)
                .message("Service package updated successfully")
                .data(contentService.updatePackage(
                        authentication.getName(),
                        serviceId,
                        packageId,
                        request
                ))
                .build();
    }

    @DeleteMapping("/packages/{packageId}")
    public ApiResponse<Object> deletePackage(
            Authentication authentication,
            @PathVariable Long serviceId,
            @PathVariable Long packageId
    ) {
        contentService.deletePackage(
                authentication.getName(),
                serviceId,
                packageId
        );

        return ApiResponse.builder()
                .success(true)
                .message("Service package deleted successfully")
                .data(null)
                .build();
    }
}