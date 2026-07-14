package com.weddingstore.marketplace.availability.entity;

import com.weddingstore.common.entity.BaseEntity;
import com.weddingstore.marketplace.service.entity.ServiceOffering;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "service_blocked_dates",
        indexes = {
                @Index(
                        name = "idx_blocked_date_service",
                        columnList = "service_id"
                ),
                @Index(
                        name = "idx_blocked_date",
                        columnList = "blocked_date"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_service_blocked_date",
                        columnNames = {
                                "service_id",
                                "blocked_date"
                        }
                )
        }
)
public class ServiceBlockedDate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceOffering service;

    @Column(name = "blocked_date", nullable = false)
    private LocalDate blockedDate;

    @Column(length = 500)
    private String reason;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}