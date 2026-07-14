package com.weddingstore.ai.dto;

import com.weddingstore.marketplace.product.dto.ProductResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AiProductRecommendationResponse {

    private Long eventId;

    private String recommendation;

    private List<ProductResponse> products;
}