package com.weddingstore.marketplace.product.repository;

import com.weddingstore.marketplace.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository
        extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    boolean existsBySlug(String slug);

    boolean existsBySku(String sku);

    List<Product> findByActiveTrueOrderByIdDesc();

    List<Product>
    findByCategoryIdAndActiveTrueOrderByIdDesc(Long categoryId);

    List<Product>
    findByFeaturedTrueAndActiveTrueOrderByIdDesc();

    List<Product>
    findTop10ByActiveTrueOrderByFeaturedDescIdDesc();

    List<Product>
    findTop10ByCategoryIdAndActiveTrueOrderByFeaturedDescIdDesc(
            Long categoryId
    );
}