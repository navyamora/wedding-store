package com.weddingstore.marketplace.search.specification;

import com.weddingstore.marketplace.service.entity.ServiceLocationType;
import com.weddingstore.marketplace.service.entity.ServiceOffering;
import com.weddingstore.marketplace.service.entity.ServiceType;
import com.weddingstore.planning.event.entity.EventType;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Locale;

public final class ServiceOfferingSpecification {

    private ServiceOfferingSpecification() {
    }

    public static Specification<ServiceOffering> active() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("active"));
    }

    public static Specification<ServiceOffering> approvedVendor() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("vendor").get("status"),
                        com.weddingstore.marketplace.vendor.entity
                                .VendorStatus.APPROVED
                );
    }

    public static Specification<ServiceOffering> keyword(
            String keyword
    ) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String pattern = "%"
                    + keyword.trim()
                    .toLowerCase(Locale.ROOT)
                    + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("shortDescription")
                            ),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("description")
                            ),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("vendor")
                                            .get("businessName")
                            ),
                            pattern
                    )
            );
        };
    }

    public static Specification<ServiceOffering> city(
            String city
    ) {
        return (root, query, criteriaBuilder) -> {
            if (city == null || city.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("city")),
                    city.trim().toLowerCase(Locale.ROOT)
            );
        };
    }

    public static Specification<ServiceOffering> category(
            Long categoryId
    ) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("category").get("id"),
                    categoryId
            );
        };
    }

    public static Specification<ServiceOffering> vendor(
            Long vendorId
    ) {
        return (root, query, criteriaBuilder) -> {
            if (vendorId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("vendor").get("id"),
                    vendorId
            );
        };
    }

    public static Specification<ServiceOffering> serviceType(
            ServiceType serviceType
    ) {
        return (root, query, criteriaBuilder) -> {
            if (serviceType == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("serviceType"),
                    serviceType
            );
        };
    }

    public static Specification<ServiceOffering> locationType(
            ServiceLocationType locationType
    ) {
        return (root, query, criteriaBuilder) -> {
            if (locationType == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("locationType"),
                    locationType
            );
        };
    }

    public static Specification<ServiceOffering> eventType(
            EventType eventType
    ) {
        return (root, query, criteriaBuilder) -> {
            if (eventType == null) {
                return criteriaBuilder.conjunction();
            }

            query.distinct(true);

            var eventTypeJoin = root.join(
                    "applicableEventTypes",
                    JoinType.LEFT
            );

            return criteriaBuilder.or(
                    criteriaBuilder.equal(
                            eventTypeJoin,
                            eventType
                    ),
                    criteriaBuilder.isEmpty(
                            root.get("applicableEventTypes")
                    )
            );
        };
    }

    public static Specification<ServiceOffering> minPrice(
            BigDecimal minPrice
    ) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("basePrice"),
                    minPrice
            );
        };
    }

    public static Specification<ServiceOffering> maxPrice(
            BigDecimal maxPrice
    ) {
        return (root, query, criteriaBuilder) -> {
            if (maxPrice == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("basePrice"),
                    maxPrice
            );
        };
    }

    public static Specification<ServiceOffering> minRating(
            BigDecimal minRating
    ) {
        return (root, query, criteriaBuilder) -> {
            if (minRating == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("rating"),
                    minRating
            );
        };
    }

    public static Specification<ServiceOffering> featured(
            Boolean featured
    ) {
        return (root, query, criteriaBuilder) -> {
            if (featured == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("featured"),
                    featured
            );
        };
    }
}