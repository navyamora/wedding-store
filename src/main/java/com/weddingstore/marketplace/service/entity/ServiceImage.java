package com.weddingstore.marketplace.service.entity;

import com.weddingstore.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "service_images",
        indexes = {
                @Index(name = "idx_service_images_service", columnList = "service_id")
        }
)
public class ServiceImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceOffering service;

    @Column(name = "image_url", nullable = false, length = 1000)
    private String imageUrl;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Builder.Default
    @Column(name = "primary_image", nullable = false)
    private Boolean primaryImage = false;

    @Column(length = 250)
    private String altText;
}