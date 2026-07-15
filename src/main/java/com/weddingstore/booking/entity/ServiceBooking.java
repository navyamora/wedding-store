package com.weddingstore.booking.entity;

import com.weddingstore.common.entity.BaseEntity;
import com.weddingstore.marketplace.service.entity.ServiceOffering;
import com.weddingstore.marketplace.service.entity.ServicePackage;
import com.weddingstore.planning.event.entity.Event;
import com.weddingstore.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "service_bookings",
        indexes = {
                @Index(
                        name = "idx_booking_customer",
                        columnList = "customer_id"
                ),
                @Index(
                        name = "idx_booking_service",
                        columnList = "service_id"
                ),
                @Index(
                        name = "idx_booking_event",
                        columnList = "event_id"
                ),
                @Index(
                        name = "idx_booking_date",
                        columnList = "requested_date"
                ),
                @Index(
                        name = "idx_booking_status",
                        columnList = "status"
                )
        }
)
public class ServiceBooking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceOffering service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_package_id")
    private ServicePackage servicePackage;

    @Column(name = "requested_date", nullable = false)
    private LocalDate requestedDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(nullable = false, length = 500)
    private String location;

    @Column(name = "customer_notes", columnDefinition = "TEXT")
    private String customerNotes;

    @Column(name = "vendor_message", columnDefinition = "TEXT")
    private String vendorMessage;

    @Column(
            name = "estimated_amount",
            nullable = false,
            precision = 14,
            scale = 2
    )
    private BigDecimal estimatedAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private BookingStatus status = BookingStatus.REQUESTED;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}