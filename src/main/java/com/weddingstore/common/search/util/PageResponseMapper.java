package com.weddingstore.common.search.util;

import com.weddingstore.common.search.dto.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public final class PageResponseMapper {

    private PageResponseMapper() {
    }

    public static <E, D> PageResponse<D> map(
            Page<E> page,
            Function<E, D> mapper,
            String sortBy,
            String direction
    ) {
        List<D> content = page.getContent()
                .stream()
                .map(mapper)
                .toList();

        return PageResponse.<D>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .sortBy(sortBy)
                .direction(direction)
                .build();
    }
}