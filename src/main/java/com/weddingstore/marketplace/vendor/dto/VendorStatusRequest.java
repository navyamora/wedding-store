package com.weddingstore.marketplace.vendor.dto;

import com.weddingstore.marketplace.vendor.entity.VendorStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VendorStatusRequest {

    @NotNull
    private VendorStatus status;

    @Size(max = 500)
    private String reason;
}