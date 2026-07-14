package com.weddingstore.marketplace.service.repository;

import com.weddingstore.marketplace.service.entity.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicePackageRepository
        extends JpaRepository<ServicePackage, Long> {

    List<ServicePackage>
    findByServiceIdAndActiveTrueOrderByDisplayOrderAsc(Long serviceId);

    Optional<ServicePackage>
    findByIdAndServiceIdAndActiveTrue(
            Long packageId,
            Long serviceId
    );
}