package com.weddingstore.common.search.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SearchRequest {

    private String keyword;

    @Min(0)
    private Integer page = 0;

    @Min(1)
    @Max(100)
    private Integer size = 20;

    private String sortBy = "createdAt";

    private String direction = "DESC";
}