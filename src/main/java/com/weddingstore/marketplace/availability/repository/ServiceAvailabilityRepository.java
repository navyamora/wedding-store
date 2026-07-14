package com.weddingstore.marketplace.availability.repository;

import com.weddingstore.marketplace.availability.entity.ServiceAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface ServiceAvailabilityRepository
        extends JpaRepository<ServiceAvailability, Long> {

    List<ServiceAvailability>
    findByServiceIdAndActiveTrueOrderByDayOfWeekAscStartTimeAsc(
            Long serviceId
    );

    Optional<ServiceAvailability>
    findByIdAndServiceIdAndActiveTrue(
            Long availabilityId,
            Long serviceId
    );

    boolean existsByServiceIdAndDayOfWeekAndActiveTrue(
            Long serviceId,
            DayOfWeek dayOfWeek
    );
}