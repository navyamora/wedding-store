package com.weddingstore.ai.prompt;

import com.weddingstore.ai.dto.BudgetPlannerRequest;
import com.weddingstore.ai.dto.ChecklistRequest;

public final class PromptBuilder {

    private PromptBuilder() {
    }

    public static String chat(String message) {
        return """
                %s

                Customer question:
                %s
                """.formatted(
                SystemPrompts.WEDDING_ASSISTANT,
                sanitize(message)
        );
    }

    public static String budgetPlanner(BudgetPlannerRequest request) {
        return """
                %s

                Wedding details:

                City: %s
                Total budget: ₹%s
                Number of guests: %d
                Wedding type: %s
                Preferences: %s

                Generate the complete estimated wedding budget now.
                """.formatted(
                SystemPrompts.BUDGET_PLANNER,
                sanitize(request.getCity()),
                request.getBudget().toPlainString(),
                request.getGuests(),
                valueOrDefault(request.getWeddingType(), "Not specified"),
                valueOrDefault(request.getPreferences(), "Not specified")
        );
    }

    public static String weddingChecklist(ChecklistRequest request) {
        return """
                %s

                Wedding details:

                City: %s
                Wedding date: %s
                Number of guests: %d
                Wedding type: %s
                Current planning stage: %s
                Completed tasks: %s
                Additional preferences: %s

                Today's planning context:
                Create the checklist relative to the supplied wedding date.
                Start with the most urgent incomplete tasks.
                """.formatted(
                SystemPrompts.WEDDING_CHECKLIST,
                sanitize(request.getCity()),
                request.getWeddingDate(),
                request.getGuests(),
                valueOrDefault(request.getWeddingType(), "Not specified"),
                valueOrDefault(request.getCurrentStage(), "Planning has not started"),
                valueOrDefault(request.getCompletedTasks(), "None specified"),
                valueOrDefault(request.getPreferences(), "None specified")
        );
    }

    private static String valueOrDefault(String value, String defaultValue) {
        return value == null || value.isBlank()
                ? defaultValue
                : sanitize(value);
    }

    private static String sanitize(String value) {
        if (value == null) {
            return "";
        }

        return value
                .trim()
                .replace("\u0000", "");
    }
}