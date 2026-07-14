package com.weddingstore.marketplace.service.repository;

import com.weddingstore.marketplace.service.entity.ServiceOffering;
import com.weddingstore.marketplace.service.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ServiceOfferingRepository
        extends JpaRepository<ServiceOffering, Long> {

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    List<ServiceOffering>
    findByActiveTrueOrderByCreatedAtDesc();

    Optional<ServiceOffering>
    findByIdAndActiveTrue(Long id);

    List<ServiceOffering>
    findByVendorIdAndActiveTrueOrderByCreatedAtDesc(Long vendorId);

    Optional<ServiceOffering>
    findByIdAndVendorIdAndActiveTrue(
            Long id,
            Long vendorId
    );

    List<ServiceOffering>
    findByCityIgnoreCaseAndActiveTrueOrderByRatingDesc(
            String city
    );

    List<ServiceOffering>
    findByServiceTypeAndActiveTrueOrderByRatingDesc(
            ServiceType serviceType
    );

    List<ServiceOffering>
    findByCategoryIdAndActiveTrueOrderByCreatedAtDesc(
            Long categoryId
    );

    List<ServiceOffering>
    findByBasePriceLessThanEqualAndActiveTrueOrderByBasePriceAsc(
            BigDecimal maxPrice
    );

    List<ServiceOffering>
    findByFeaturedTrueAndActiveTrueOrderByRatingDesc();
}