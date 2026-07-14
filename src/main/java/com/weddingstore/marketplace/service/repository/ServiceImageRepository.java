package com.weddingstore.marketplace.service.repository;

import com.weddingstore.marketplace.service.entity.ServiceImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceImageRepository
        extends JpaRepository<ServiceImage, Long> {

    List<ServiceImage>
    findByServiceIdOrderByDisplayOrderAsc(Long serviceId);

    Optional<ServiceImage>
    findByIdAndServiceId(Long imageId, Long serviceId);

    long countByServiceId(Long serviceId);
}