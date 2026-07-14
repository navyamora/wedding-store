package com.weddingstore.marketplace.availability.repository;

import com.weddingstore.marketplace.availability.entity.ServiceBlockedDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ServiceBlockedDateRepository
        extends JpaRepository<ServiceBlockedDate, Long> {

    List<ServiceBlockedDate>
    findByServiceIdAndActiveTrueAndBlockedDateGreaterThanEqualOrderByBlockedDateAsc(
            Long serviceId,
            LocalDate fromDate
    );

    Optional<ServiceBlockedDate>
    findByIdAndServiceIdAndActiveTrue(
            Long blockedDateId,
            Long serviceId
    );

    boolean existsByServiceIdAndBlockedDateAndActiveTrue(
            Long serviceId,
            LocalDate blockedDate
    );
}