package com.weddingstore.marketplace.vendor.service;

import com.weddingstore.marketplace.vendor.dto.CreateVendorRequest;
import com.weddingstore.marketplace.vendor.dto.UpdateVendorRequest;
import com.weddingstore.marketplace.vendor.dto.VendorResponse;
import com.weddingstore.marketplace.vendor.entity.VendorStatus;

import java.util.List;

public interface VendorService {

    VendorResponse createMyProfile(
            String userEmail,
            CreateVendorRequest request
    );

    VendorResponse getMyProfile(String userEmail);

    VendorResponse updateMyProfile(
            String userEmail,
            UpdateVendorRequest request
    );

    List<VendorResponse> getApprovedVendors(String city);

    List<VendorResponse> getAllForAdmin(VendorStatus status);

    VendorResponse updateStatus(
            Long vendorId,
            VendorStatus status
    );
}