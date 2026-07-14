package com.weddingstore.ai.recommendation;

import com.weddingstore.ai.dto.AiProductRecommendationResponse;
import com.weddingstore.ai.dto.AiServiceRecommendationResponse;
import com.weddingstore.ai.dto.EventRecommendationRequest;

public interface EventRecommendationService {

    AiProductRecommendationResponse recommendProducts(
            String userEmail,
            Long eventId,
            EventRecommendationRequest request
    );

    AiServiceRecommendationResponse recommendServices(
            String userEmail,
            Long eventId,
            EventRecommendationRequest request
    );
}