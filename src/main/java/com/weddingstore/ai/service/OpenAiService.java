package com.weddingstore.ai.service;

import com.openai.client.OpenAIClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.weddingstore.ai.dto.BudgetPlannerRequest;
import com.weddingstore.ai.dto.ChecklistRequest;
import com.weddingstore.ai.exception.AiException;
import com.weddingstore.ai.prompt.PromptBuilder;
import com.weddingstore.common.config.properties.OpenAiProperties;
import com.weddingstore.common.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class OpenAiService implements AiService {

    private final OpenAIClient client;
    private final OpenAiProperties properties;

    public OpenAiService(
            OpenAIClient client,
            OpenAiProperties properties
    ) {
        this.client = client;
        this.properties = properties;
    }

    @Override
    public String chat(String message) {
        return generateResponse(PromptBuilder.chat(message));
    }

    @Override
    public String createBudgetPlan(BudgetPlannerRequest request) {
        return generateResponse(PromptBuilder.budgetPlanner(request));
    }

    @Override
    public String createWeddingChecklist(ChecklistRequest request) {
        return generateResponse(PromptBuilder.weddingChecklist(request));
    }
    
    @Override
    public String generateFromPrompt(String prompt) {
        return generateResponse(prompt);
    }

    private String generateResponse(String prompt) {
        try {
            ResponseCreateParams params = ResponseCreateParams.builder()
                    .model(properties.getModel())
                    .input(prompt)
                    .build();

            Response response = client.responses().create(params);

            String output = response.output()
                    .stream()
                    .flatMap(item -> item.message().stream())
                    .flatMap(message -> message.content().stream())
                    .flatMap(content -> content.outputText().stream())
                    .map(outputText -> outputText.text())
                    .collect(Collectors.joining("\n"))
                    .trim();

            if (output.isBlank()) {
                throw new AiException(
                        ErrorCode.AI_SERVICE_UNAVAILABLE,
                        "The AI service returned an empty response.", null
                );
            }

            return output;

        } catch (AiException ex) {
            throw ex;

        } catch (Exception ex) {
            throw mapOpenAiException(ex);
        }
    }

    private AiException mapOpenAiException(Exception ex) {
        String message = ex.getMessage() == null
                ? ""
                : ex.getMessage().toLowerCase();

        if (message.contains("429")
                || message.contains("quota")
                || message.contains("rate limit")) {

            return new AiException(
                    ErrorCode.AI_QUOTA_EXCEEDED,
                    "AI usage limit has been reached. Please try again later.",
                    ex
            );
        }

        if (message.contains("401")
                || message.contains("invalid api key")
                || message.contains("incorrect api key")) {

            return new AiException(
                    ErrorCode.AI_INVALID_API_KEY,
                    "The AI service credentials are invalid.",
                    ex
            );
        }

        if (message.contains("timeout")
                || message.contains("timed out")) {

            return new AiException(
                    ErrorCode.AI_SERVICE_UNAVAILABLE,
                    "The AI request timed out. Please try again.",
                    ex
            );
        }

        return new AiException(
                ErrorCode.AI_SERVICE_UNAVAILABLE,
                "The AI service is currently unavailable.",
                ex
        );
    }
}