package com.weddingstore.marketplace.vendor.repository;

import com.weddingstore.marketplace.vendor.entity.VendorProfile;
import com.weddingstore.marketplace.vendor.entity.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface VendorProfileRepository
        extends JpaRepository<VendorProfile, Long>,

        JpaSpecificationExecutor<VendorProfile>{

    boolean existsByUserId(Long userId);

    Optional<VendorProfile> findByUserIdAndActiveTrue(Long userId);

    Optional<VendorProfile> findByIdAndActiveTrue(Long id);

    List<VendorProfile> findByStatusAndActiveTrueOrderByCreatedAtDesc(
            VendorStatus status
    );

    List<VendorProfile> findByActiveTrueOrderByCreatedAtDesc();

    List<VendorProfile> findByCityIgnoreCaseAndStatusAndActiveTrueOrderByRatingDesc(
            String city,
            VendorStatus status
    );
}