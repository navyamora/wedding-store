package com.weddingstore.planning.event.entity;

import com.weddingstore.common.entity.BaseEntity;
import com.weddingstore.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "events",
        indexes = {
                @Index(name = "idx_events_user_id", columnList = "user_id"),
                @Index(name = "idx_events_date", columnList = "event_date"),
                @Index(name = "idx_events_type", columnList = "event_type")
        }
)
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 50)
    private EventType eventType;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(length = 250)
    private String venue;

    @Column(precision = 14, scale = 2)
    private BigDecimal budget;

    @Column(name = "guest_count")
    private Integer guestCount;

    @Column(length = 150)
    private String theme;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private EventStatus status = EventStatus.DRAFT;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}