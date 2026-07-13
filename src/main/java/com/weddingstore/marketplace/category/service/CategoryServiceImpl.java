package com.weddingstore.marketplace.category.service;

import com.weddingstore.marketplace.category.dto.*;
import com.weddingstore.marketplace.category.entity.Category;
import com.weddingstore.marketplace.category.mapper.CategoryMapper;
import com.weddingstore.marketplace.category.repository.CategoryRepository;
import com.weddingstore.common.exception.ConflictException;
import com.weddingstore.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponse create(CreateCategoryRequest request) {
        String slug = generateSlug(request.getName());

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ConflictException("Category name already exists");
        }

        if (categoryRepository.existsBySlug(slug)) {
            throw new ConflictException("Category slug already exists");
        }

        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .displayOrder(request.getDisplayOrder())
                .active(true)
                .build();

        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    @Override
    public List<CategoryResponse> getAllActive() {
        return categoryRepository.findByActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse getById(Long id) {
        Category category = findCategoryById(id);
        return categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse update(Long id, UpdateCategoryRequest request) {
        Category category = findCategoryById(id);

        String newSlug = generateSlug(request.getName());

        category.setName(request.getName());
        category.setSlug(newSlug);
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setDisplayOrder(request.getDisplayOrder());

        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }

        Category updated = categoryRepository.save(category);
        return categoryMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        Category category = findCategoryById(id);
        category.setActive(false);
        categoryRepository.save(category);
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll