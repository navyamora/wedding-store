package com.weddingstore.ai.controller;

import com.weddingstore.ai.dto.AiEventResponse;
import com.weddingstore.ai.service.AiEventService;
import com.weddingstore.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/events/{eventId}")
@RequiredArgsConstructor
public class EventAiController {

    private final AiEventService aiEventService;

    @PostMapping("/budget-plan")
    public ApiResponse<AiEventResponse> generateBudgetPlan(
            Authentication authentication,
            @PathVariable Long eventId
    ) {
        AiEventResponse response =
                aiEventService.generateBudgetPlan(
                        authentication.getName(),
                        eventId
                );

        return ApiResponse.<AiEventResponse>builder()
                .success(true)
                .message("Event budget plan generated successfully")
                .data(response)
                .error(null)
                .build();
    }

    @PostMapping("/checklist")
    public ApiResponse<AiEventResponse> generateChecklist(
            Authentication authentication,
            @PathVariable Long eventId
    ) {
        AiEventResponse response =
                aiEventService.generateChecklist(
                        authentication.getName(),
                        eventId
                );

        return ApiResponse.<AiEventResponse>builder()
                .success(true)
                .message("Event checklist generated successfully")
                .data(response)
                .error(null)
                .build();
    }
}