package com.weddingstore.marketplace.vendor.dto;

import com.weddingstore.marketplace.vendor.entity.VendorType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateVendorRequest {

    @NotBlank
    @Size(max = 180)
    private String businessName;

    @NotBlank
    @Size(max = 150)
    private String ownerName;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10,15}$")
    private String mobile;

    @NotNull
    private VendorType vendorType;

    @Size(max = 30)
    private String gstNumber;

    @Size(max = 20)
    private String panNumber;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 100)
    private String state;

    @NotBlank
    @Size(max = 100)
    private String country;

    @NotBlank
    @Size(max = 500)
    private String address;

    @Size(max = 5000)
    private String description;

    @Size(max = 1000)
    private String logoUrl;
}