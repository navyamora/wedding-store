package com.weddingstore.marketplace.vendor.controller;

import com.weddingstore.common.response.ApiResponse;
import com.weddingstore.marketplace.vendor.dto.VendorResponse;
import com.weddingstore.marketplace.vendor.dto.VendorStatusRequest;
import com.weddingstore.marketplace.vendor.entity.VendorStatus;
import com.weddingstore.marketplace.vendor.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/vendors")
public class AdminVendorController {

    private final VendorService vendorService;

    public AdminVendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public ApiResponse<List<VendorResponse>> getAll(
            @RequestParam(required = false) VendorStatus status
    ) {
        return ApiResponse.<List<VendorResponse>>builder()
                .success(true)
                .message("Vendor profiles fetched successfully")
                .data(vendorService.getAllForAdmin(status))
                .build();
    }

    @PutMapping("/{vendorId}/status")
    public ApiResponse<VendorResponse> updateStatus(
            @PathVariable Long vendorId,
            @Valid @RequestBody VendorStatusRequest request
    ) {
        VendorResponse vendor = vendorService.updateStatus(
                vendorId,
                request.getStatus()
        );

        return ApiResponse.<VendorResponse>builder()
                .success(true)
                .message("Vendor status updated successfully")
                .data(vendor)
                .build();
    }
}