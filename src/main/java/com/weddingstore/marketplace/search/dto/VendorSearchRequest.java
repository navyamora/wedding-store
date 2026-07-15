package com.weddingstore.marketplace.search.dto;

import com.weddingstore.common.search.dto.SearchRequest;
import com.weddingstore.marketplace.vendor.entity.VendorType;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VendorSearchRequest extends SearchRequest {

    private String city;

    private String state;

    private VendorType vendorType;

    private Boolean verified;

    @DecimalMin("0.0")
    private BigDecimal minRating;
}