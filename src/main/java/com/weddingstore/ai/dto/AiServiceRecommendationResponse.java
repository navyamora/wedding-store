package com.weddingstore.ai.dto;

import com.weddingstore.marketplace.service.dto.ServiceResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AiServiceRecommendationResponse {

    private Long eventId;

    private String recommendation;

    private List<ServiceResponse> services;
}