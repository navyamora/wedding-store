package com.weddingstore.catalog.product.service;

import com.weddingstore.catalog.product.dto.*;

import java.util.List;

public interface ProductService {

    ProductResponse create(CreateProductRequest request);

    List<ProductResponse> getAllActive(Long categoryId);

    List<ProductResponse> getFeatured();

    ProductResponse getById(Long id);

    ProductResponse update(Long id, UpdateProductRequest request);

    void delete(Long id);
}