package com.weddingstore.marketplace.product.repository;

import com.weddingstore.marketplace.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySlug(String slug);

    boolean existsBySku(String sku);

    List<Product> findByActiveTrueOrderByIdDesc();

    List<Product> findByCategoryIdAndActiveTrueOrderByIdDesc(Long categoryId);

    List<Product> findByFeaturedTrueAndActiveTrueOrderByIdDesc();
}