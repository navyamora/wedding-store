package com.weddingstore.marketplace.search.specification;

import com.weddingstore.marketplace.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Locale;

public final class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<Product> active() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("active"));
    }

    public static Specification<Product> keyword(
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
                            criteriaBuilder.lower(root.get("sku")),
                            pattern
                    )
            );
        };
    }

    public static Specification<Product> category(
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

    public static Specification<Product> minPrice(
            BigDecimal minPrice
    ) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("price"),
                    minPrice
            );
        };
    }

    public static Specification<Product> maxPrice(
            BigDecimal maxPrice
    ) {
        return (root, query, criteriaBuilder) -> {
            if (maxPrice == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("price"),
                    maxPrice
            );
        };
    }

    public static Specification<Product> featured(
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

    public static Specification<Product> inStock(
            Boolean inStock
    ) {
        return (root, query, criteriaBuilder) -> {
            if (inStock == null) {
                return criteriaBuilder.conjunction();
            }

            if (inStock) {
                return criteriaBuilder.greaterThan(
                        root.get("quantity"),
                        0
                );
            }

            return criteriaBuilder.or(
                    criteriaBuilder.isNull(root.get("quantity")),
                    criteriaBuilder.lessThanOrEqualTo(
                            root.get("quantity"),
                            0
                    )
            );
        };
    }
    
    public static Specification<Product> minEffectivePrice(
            BigDecimal minPrice
    ) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null) {
                return criteriaBuilder.conjunction();
            }

            var effectivePrice = criteriaBuilder.<BigDecimal>coalesce()
                    .value(root.get("discountPrice"))
                    .value(root.get("price"));

            return criteriaBuilder.greaterThanOrEqualTo(
                    effectivePrice,
                    minPrice
            );
        };
    }

    public static Specification<Product> maxEffectivePrice(
            BigDecimal maxPrice
    ) {
        return (root, query, criteriaBuilder) -> {
            if (maxPrice == null) {
                return criteriaBuilder.conjunction();
            }

            var effectivePrice = criteriaBuilder.<BigDecimal>coalesce()
                    .value(root.get("discountPrice"))
                    .value(root.get("price"));

            return criteriaBuilder.lessThanOrEqualTo(
                    effectivePrice,
                    maxPrice
            );
        };
    }
}