package com.weddingstore.ai.controller;

import com.weddingstore.ai.dto.BudgetPlannerRequest;
import com.weddingstore.ai.dto.BudgetPlannerResponse;
import com.weddingstore.ai.dto.ChatRequest;
import com.weddingstore.ai.dto.ChatResponse;
import com.weddingstore.ai.dto.ChecklistRequest;
import com.weddingstore.ai.dto.ChecklistResponse;
import com.weddingstore.ai.service.AiService;
import com.weddingstore.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    public ApiResponse<ChatResponse> chat(
            @Valid @RequestBody ChatRequest request) {

        String response = aiService.chat(request.getMessage());

        return ApiResponse.<ChatResponse>builder()
                .success(true)
                .message("AI response generated")
                .data(ChatResponse.builder()
                        .response(response)
                        .build())
                .build();
    }
    @PostMapping("/budget-planner")
    public ApiResponse<BudgetPlannerResponse> createBudgetPlan(
            @Valid @RequestBody BudgetPlannerRequest request
    ) {
        String plan = aiService.createBudgetPlan(request);

        return ApiResponse.<BudgetPlannerResponse>builder()
                .success(true)
                .message("Wedding budget plan generated successfully")
                .data(BudgetPlannerResponse.builder()
                        .plan(plan)
                        .build())
                .error(null)
                .build();
    }
    @PostMapping("/checklist")
    public ApiResponse<ChecklistResponse> createChecklist(
            @Valid @RequestBody ChecklistRequest request
    ) {
        String result = aiService.createWeddingChecklist(request);

        return ApiResponse.<ChecklistResponse>builder()
                .success(true)
                .message("Wedding checklist generated successfully")
                .data(ChecklistResponse.builder()
                        .checklist(result)
                        .build())
                .error(null)
                .build();
    }
    
    
}