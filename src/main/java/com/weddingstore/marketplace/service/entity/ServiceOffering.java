package com.weddingstore.marketplace.service.entity;

import com.weddingstore.common.entity.BaseEntity;
import com.weddingstore.marketplace.availability.entity.ServiceAvailability;
import com.weddingstore.marketplace.availability.entity.ServiceBlockedDate;
import com.weddingstore.marketplace.category.entity.Category;
import com.weddingstore.marketplace.vendor.entity.VendorProfile;
import com.weddingstore.planning.event.entity.EventType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "service_offerings",
        indexes = {
                @Index(
                        name = "idx_service_vendor",
                        columnList = "vendor_id"
                ),
                @Index(
                        name = "idx_service_category",
                        columnList = "category_id"
                ),
                @Index(
                        name = "idx_service_type",
                        columnList = "service_type"
                ),
                @Index(
                        name = "idx_service_city",
                        columnList = "city"
                ),
                @Index(
                        name = "idx_service_active",
                        columnList = "active"
                )
        }
)
public class ServiceOffering extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    private VendorProfile vendor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "service_type",
            nullable = false,
            length = 50
    )
    private ServiceType serviceType;

    @Column(nullable = false, length = 180)
    private String name;

    @Column(nullable = false, unique = true, length = 220)
    private String slug;

    @Column(length = 500)
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(
            name = "base_price",
            nullable = false,
            precision = 14,
            scale = 2
    )
    private BigDecimal basePrice;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "location_type",
            nullable = false,
            length = 40
    )
    private ServiceLocationType locationType;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(name = "service_radius_km")
    private Integer serviceRadiusKm;

    @Column(name = "advance_percentage", precision = 5, scale = 2)
    private BigDecimal advancePercentage;

    @Builder.Default
    @Column(nullable = false)
    private Boolean featured = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Builder.Default
    @Column(precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.ZERO;

    @ElementCollection(targetClass = EventType.class)
    @CollectionTable(
            name = "service_event_types",
            joinColumns = @JoinColumn(name = "service_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", length = 50)
    @Builder.Default
    private Set<EventType> applicableEventTypes = new HashSet<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "service",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("displayOrder ASC")
    private java.util.List<ServiceImage> images =
            new java.util.ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "service",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("displayOrder ASC")
    private java.util.List<ServicePackage> packages =
            new java.util.ArrayList<>();
    
    @Builder.Default
    @OneToMany(
            mappedBy = "service",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private java.util.List<ServiceAvailability> availability =
            new java.util.ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "service",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private java.util.List<ServiceBlockedDate> blockedDates =
            new java.util.ArrayList<>();
}