package com.weddingstore.ai.service;

import com.weddingstore.ai.dto.BudgetPlannerRequest;
import com.weddingstore.ai.dto.ChecklistRequest;

public interface AiService {

    String chat(String message);
    
    String createBudgetPlan(BudgetPlannerRequest request);
    
    String createWeddingChecklist(ChecklistRequest request);
    String generateFromPrompt(String prompt);
}