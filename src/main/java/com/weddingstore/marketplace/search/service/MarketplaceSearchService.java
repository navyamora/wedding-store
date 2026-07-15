package com.weddingstore.marketplace.search.service;

import com.weddingstore.common.search.dto.PageResponse;
import com.weddingstore.marketplace.product.dto.ProductResponse;
import com.weddingstore.marketplace.search.dto.ProductSearchRequest;
import com.weddingstore.marketplace.search.dto.ServiceSearchRequest;
import com.weddingstore.marketplace.search.dto.VendorSearchRequest;
import com.weddingstore.marketplace.service.dto.ServiceResponse;
import com.weddingstore.marketplace.vendor.dto.VendorResponse;

public interface MarketplaceSearchService {

    PageResponse<ProductResponse> searchProducts(
            ProductSearchRequest request
    );

    PageResponse<ServiceResponse> searchServices(
            ServiceSearchRequest request
    );

    PageResponse<VendorResponse> searchVendors(
            VendorSearchRequest request
    );
}