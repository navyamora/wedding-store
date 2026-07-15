package com.weddingstore.marketplace.search.specification;

import com.weddingstore.marketplace.vendor.entity.VendorProfile;
import com.weddingstore.marketplace.vendor.entity.VendorStatus;
import com.weddingstore.marketplace.vendor.entity.VendorType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Locale;

public final class VendorSpecification {

    private VendorSpecification() {
    }

    public static Specification<VendorProfile> active() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("active"));
    }

    public static Specification<VendorProfile> approved() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        VendorStatus.APPROVED
                );
    }

    public static Specification<VendorProfile> keyword(
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
                            criteriaBuilder.lower(
                                    root.get("businessName")
                            ),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("ownerName")
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
                            criteriaBuilder.lower(root.get("city")),
                            pattern
                    )
            );
        };
    }

    public static Specification<VendorProfile> city(
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

    public static Specification<VendorProfile> state(
            String state
    ) {
        return (root, query, criteriaBuilder) -> {
            if (state == null || state.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("state")),
                    state.trim().toLowerCase(Locale.ROOT)
            );
        };
    }

    public static Specification<VendorProfile> vendorType(
            VendorType vendorType
    ) {
        return (root, query, criteriaBuilder) -> {
            if (vendorType == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("vendorType"),
                    vendorType
            );
        };
    }

    public static Specification<VendorProfile> verified(
            Boolean verified
    ) {
        return (root, query, criteriaBuilder) -> {
            if (verified == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("verified"),
                    verified
            );
        };
    }

    public static Specification<VendorProfile> minRating(
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
}