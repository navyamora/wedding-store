package com.weddingstore.marketplace.service.repository;

import com.weddingstore.marketplace.service.entity.ServiceOffering;
import com.weddingstore.marketplace.service.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ServiceOfferingRepository
        extends JpaRepository<ServiceOffering, Long>,
JpaSpecificationExecutor<ServiceOffering> {

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
    
    List<ServiceOffering>
    findTop10ByActiveTrueOrderByFeaturedDescRatingDesc();

    List<ServiceOffering>
    findTop10ByCategoryIdAndActiveTrueOrderByFeaturedDescRatingDesc(
            Long categoryId
    );

    List<ServiceOffering>
    findTop10ByCityIgnoreCaseAndActiveTrueOrderByFeaturedDescRatingDesc(
            String city
    );
}