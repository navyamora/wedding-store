package com.weddingstore.marketplace.search.controller;

import com.weddingstore.common.response.ApiResponse;
import com.weddingstore.common.search.dto.PageResponse;
import com.weddingstore.marketplace.product.dto.ProductResponse;
import com.weddingstore.marketplace.search.dto.ProductSearchRequest;
import com.weddingstore.marketplace.search.dto.ServiceSearchRequest;
import com.weddingstore.marketplace.search.dto.VendorSearchRequest;
import com.weddingstore.marketplace.search.service.MarketplaceSearchService;
import com.weddingstore.marketplace.service.dto.ServiceResponse;
import com.weddingstore.marketplace.vendor.dto.VendorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class MarketplaceSearchController {

    private final MarketplaceSearchService searchService;

    @GetMapping("/products")
    public ApiResponse<PageResponse<ProductResponse>> searchProducts(
            @Valid @ModelAttribute ProductSearchRequest request
    ) {
        return ApiResponse
                .<PageResponse<ProductResponse>>builder()
                .success(true)
                .message("Products fetched successfully")
                .data(searchService.searchProducts(request))
                .build();
    }

    @GetMapping("/services")
    public ApiResponse<PageResponse<ServiceResponse>> searchServices(
            @Valid @ModelAttribute ServiceSearchRequest request
    ) {
        return ApiResponse
                .<PageResponse<ServiceResponse>>builder()
                .success(true)
                .message("Services fetched successfully")
                .data(searchService.searchServices(request))
                .build();
    }

    @GetMapping("/vendors")
    public ApiResponse<PageResponse<VendorResponse>> searchVendors(
            @Valid @ModelAttribute VendorSearchRequest request
    ) {
        return ApiResponse
                .<PageResponse<VendorResponse>>builder()
                .success(true)
                .message("Vendors fetched successfully")
                .data(searchService.searchVendors(request))
                .build();
    }
}