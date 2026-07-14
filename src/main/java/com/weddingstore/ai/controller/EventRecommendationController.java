package com.weddingstore.ai.controller;

import com.weddingstore.ai.dto.AiProductRecommendationResponse;
import com.weddingstore.ai.dto.AiServiceRecommendationResponse;
import com.weddingstore.ai.dto.EventRecommendationRequest;
import com.weddingstore.ai.recommendation.EventRecommendationService;
import com.weddingstore.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/events/{eventId}")
public class EventRecommendationController {

    private final EventRecommendationService recommendationService;

    public EventRecommendationController(
            EventRecommendationService recommendationService
    ) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/product-recommendations")
    public ApiResponse<AiProductRecommendationResponse>
    recommendProducts(
            Authentication authentication,
            @PathVariable Long eventId,
            @Valid @RequestBody EventRecommendationRequest request
    ) {
        return ApiResponse
                .<AiProductRecommendationResponse>builder()
                .success(true)
                .message(
                        "Product recommendations generated successfully"
                )
                .data(
                        recommendationService.recommendProducts(
                                authentication.getName(),
                                eventId,
                                request
                        )
                )
                .build();
    }

    @PostMapping("/service-recommendations")
    public ApiResponse<AiServiceRecommendationResponse>
    recommendServices(
            Authentication authentication,
            @PathVariable Long eventId,
            @Valid @RequestBody EventRecommendationRequest request
    ) {
        return ApiResponse
                .<AiServiceRecommendationResponse>builder()
                .success(true)
                .message(
                        "Service recommendations generated successfully"
                )
                .data(
                        recommendationService.recommendServices(
                                authentication.getName(),
                                eventId,
                                request
                        )
                )
                .build();
    }
}