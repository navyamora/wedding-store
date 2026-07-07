package com.weddingstore.catalog.category.repository;

import com.weddingstore.catalog.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsBySlug(String slug);

    List<Category> findByActiveTrueOrderByDisplayOrderAsc();
}