package com.weddingstore.marketplace.category.service;

import com.weddingstore.marketplace.category.dto.*;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CreateCategoryRequest request);

    List<CategoryResponse> getAllActive();

    CategoryResponse getById(Long id);

    CategoryResponse update(Long id, UpdateCategoryRequest request);

    void delete(Long id);
}