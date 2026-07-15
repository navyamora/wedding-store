package com.weddingstore.common.search.util;

import com.weddingstore.common.search.dto.SearchRequest;
import com.weddingstore.common.exception.ConflictException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

public final class PageableUtil {

    private PageableUtil() {
    }

    public static Pageable create(
            SearchRequest request,
            Set<String> allowedSortFields
    ) {
        int page = request.getPage() == null
                ? 0
                : request.getPage();

        int size = request.getSize() == null
                ? 20
                : Math.min(request.getSize(), 100);

        String sortBy = normalizeSortBy(
                request.getSortBy(),
                allowedSortFields
        );

        Sort.Direction direction = parseDirection(
                request.getDirection()
        );

        return PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );
    }

    private static String normalizeSortBy(
            String sortBy,
            Set<String> allowedSortFields
    ) {
        String resolvedSortBy =
                sortBy == null || sortBy.isBlank()
                        ? "createdAt"
                        : sortBy.trim();

        if (!allowedSortFields.contains(resolvedSortBy)) {
            throw new ConflictException(
                    "Unsupported sort field: " + resolvedSortBy
            );
        }

        return resolvedSortBy;
    }

    private static Sort.Direction parseDirection(
            String direction
    ) {
        if (direction == null || direction.isBlank()) {
            return Sort.Direction.DESC;
        }

        try {
            return Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException ex) {
            throw new ConflictException(
                    "Sort direction must be ASC or DESC"
            );
        }
    }
}