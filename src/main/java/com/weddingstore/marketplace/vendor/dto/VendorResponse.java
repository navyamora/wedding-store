package com.weddingstore.marketplace.vendor.dto;

import com.weddingstore.marketplace.vendor.entity.VendorStatus;
import com.weddingstore.marketplace.vendor.entity.VendorType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class VendorResponse {

    private Long id;
    private Long userId;
    private String businessName;
    private String ownerName;
    private String email;
    private String mobile;
    private VendorType vendorType;
    private String gstNumber;
    private String panNumber;
    private String city;
    private String state;
    private String country;
    private String address;
    private String description;
    private String logoUrl;
    private VendorStatus status;
    private Boolean verified;
    private Boolean active;
    private BigDecimal rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}