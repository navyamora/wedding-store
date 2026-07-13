package com.weddingstore.marketplace.vendor.controller;

import com.weddingstore.common.response.ApiResponse;
import com.weddingstore.marketplace.vendor.dto.CreateVendorRequest;
import com.weddingstore.marketplace.vendor.dto.UpdateVendorRequest;
import com.weddingstore.marketplace.vendor.dto.VendorResponse;
import com.weddingstore.marketplace.vendor.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping("/profile")
    public ApiResponse<VendorResponse> createProfile(
            Authentication authentication,
            @Valid @RequestBody CreateVendorRequest request
    ) {
        VendorResponse vendor = vendorService.createMyProfile(
                authentication.getName(),
                request
        );

        return ApiResponse.<VendorResponse>builder()
                .success(true)
                .message("Vendor profile created successfully")
                .data(vendor)
                .build();
    }

    @GetMapping("/profile")
    public ApiResponse<VendorResponse> getMyProfile(
            Authentication authentication
    ) {
        return ApiResponse.<VendorResponse>builder()
                .success(true)
                .message("Vendor profile fetched successfully")
                .data(
                        vendorService.getMyProfile(
                                authentication.getName()
                        )
                )
                .build();
    }

    @PutMapping("/profile")
    public ApiResponse<VendorResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateVendorRequest request
    ) {
        VendorResponse vendor = vendorService.updateMyProfile(
                authentication.getName(),
                request
        );

        return ApiResponse.<VendorResponse>builder()
                .success(true)
                .message("Vendor profile updated successfully")
                .data(vendor)
                .build();
    }

    @GetMapping
    public ApiResponse<List<VendorResponse>> getApprovedVendors(
            @RequestParam(required = false) String city
    ) {
        return ApiResponse.<List<VendorResponse>>builder()
                .success(true)
                .message("Vendors fetched successfully")
                .data(vendorService.getApprovedVendors(city))
                .build();
    }
}