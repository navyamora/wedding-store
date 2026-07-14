package com.weddingstore.marketplace.availability.entity;

import com.weddingstore.common.entity.BaseEntity;
import com.weddingstore.marketplace.service.entity.ServiceOffering;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "service_availability",
        indexes = {
                @Index(
                        name = "idx_availability_service",
                        columnList = "service_id"
                ),
                @Index(
                        name = "idx_availability_day",
                        columnList = "day_of_week"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_service_day_start_end",
                        columnNames = {
                                "service_id",
                                "day_of_week",
                                "start_time",
                                "end_time"
                        }
                )
        }
)
public class ServiceAvailability extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceOffering service;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 15)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "slot_duration_minutes", nullable = false)
    private Integer slotDurationMinutes;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}