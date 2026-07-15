package com.weddingstore.ai.service;

import com.weddingstore.ai.dto.AiEventResponse;

public interface AiEventService {

    AiEventResponse generateBudgetPlan(
            String userEmail,
            Long eventId
    );

    AiEventResponse generateChecklist(
            String userEmail,
            Long eventId
    );
}