package com.weddingstore.booking.repository;

import com.weddingstore.booking.entity.BookingStatus;
import com.weddingstore.booking.entity.ServiceBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ServiceBookingRepository
        extends JpaRepository<ServiceBooking, Long> {

    List<ServiceBooking>
    findByCustomerIdAndActiveTrueOrderByCreatedAtDesc(
            Long customerId
    );

    Optional<ServiceBooking>
    findByIdAndCustomerIdAndActiveTrue(
            Long bookingId,
            Long customerId
    );

    List<ServiceBooking>
    findByServiceVendorIdAndActiveTrueOrderByCreatedAtDesc(
            Long vendorId
    );

    Optional<ServiceBooking>
    findByIdAndServiceVendorIdAndActiveTrue(
            Long bookingId,
            Long vendorId
    );

    boolean existsByServiceIdAndRequestedDateAndStartTimeAndStatusIn(
            Long serviceId,
            LocalDate requestedDate,
            LocalTime startTime,
            Collection<BookingStatus> statuses
    );
}