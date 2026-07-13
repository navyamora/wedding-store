package com.weddingstore.marketplace.category.controller;

import com.weddingstore.marketplace.category.dto.*;
import com.weddingstore.marketplace.category.service.CategoryService;
import com.weddingstore.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/api/admin/categories")
    public ApiResponse<CategoryResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Category created successfully")
                .data(categoryService.create(request))
                .build();
    }

    @GetMapping("/api/categories")
    public ApiResponse<List<CategoryResponse>> getAllActive() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .success(true)
                .message("Categories fetched successfully")
                .data(categoryService.getAllActive())
                .build();
    }

    @GetMapping("/api/categories/{id}")
    public ApiResponse<CategoryResponse> getById(@PathVariable Long id) {
        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Category fetched successfully")
                .data(categoryService.getById(id))
                .build();
    }

    @PutMapping("/api/admin/categories/{id}")
    public ApiResponse<CategoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Category updated successfully")
                .data(categoryService.update(id, request))
                .build();
    }

    @DeleteMapping("/api/admin/categories/{id}")
    public ApiResponse<Object> delete(@PathVariable Long id) {
        categoryService.delete(id);

        return ApiResponse.builder()
                .success(true)
                .message("Category deleted successfully")
                .data(null)
                .build();
    }
}