package com.weddingstore.marketplace.product.controller;

import com.weddingstore.marketplace.product.dto.*;
import com.weddingstore.marketplace.product.service.ProductService;
import com.weddingstore.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/admin/products")
    public ApiResponse<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product created successfully")
                .data(productService.create(request))
                .build();
    }

    @GetMapping("/api/products")
    public ApiResponse<List<ProductResponse>> getAll(@RequestParam(required = false) Long categoryId) {
        return ApiResponse.<List<ProductResponse>>builder()
                .success(true)
                .message("Products fetched successfully")
                .data(productService.getAllActive(categoryId))
                .build();
    }

    @GetMapping("/api/products/featured")
    public ApiResponse<List<ProductResponse>> getFeatured() {
        return ApiResponse.<List<ProductResponse>>builder()
                .success(true)
                .message("Featured products fetched successfully")
                .data(productService.getFeatured())
                .build();
    }

    @GetMapping("/api/products/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable Long id) {
        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product fetched successfully")
                .data(productService.getById(id))
                .build();
    }

    @PutMapping("/api/admin/products/{id}")
    public ApiResponse<ProductResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product updated successfully")
                .data(productService.update(id, request))
                .build();
    }

    @DeleteMapping("/api/admin/products/{id}")
    public ApiResponse<Object> delete(@PathVariable Long id) {
        productService.delete(id);

        return ApiResponse.builder()
                .success(true)
                .message("Product deleted successfully")
                .data(null)
                .build();
    }
}