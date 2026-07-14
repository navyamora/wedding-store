package com.weddingstore.ai.prompt;

import com.weddingstore.ai.dto.BudgetPlannerRequest;
import com.weddingstore.ai.dto.ChecklistRequest;
import com.weddingstore.ai.dto.EventRecommendationRequest;
import com.weddingstore.marketplace.product.entity.Product;
import com.weddingstore.marketplace.service.entity.ServiceOffering;
import com.weddingstore.planning.event.entity.Event;

import java.util.List;
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
    
    public static String productRecommendations(
            Event event,
            EventRecommendationRequest request,
            List<Product> products
    ) {
        String productContext = products.isEmpty()
                ? "No matching products were found."
                : buildProductContext(products);

        return """
                You are an event marketplace shopping assistant.

                Recommend products only from the supplied marketplace catalog.
                Never invent products, prices, IDs or availability.

                Event details:
                Event ID: %d
                Event type: %s
                Title: %s
                Date: %s
                City: %s
                Budget: %s
                Guest count: %s
                Theme: %s

                Customer preferences:
                %s

                Available marketplace products:
                %s

                Instructions:
                - Recommend the most relevant products from the supplied list.
                - Mention each recommended product's exact ID, name and price.
                - Explain why it fits the event.
                - Respect the customer's maximum price when one is supplied.
                - Do not mention any product outside the supplied catalog.
                - If none are suitable, clearly say so.
                """.formatted(
                event.getId(),
                event.getEventType(),
                sanitize(event.getTitle()),
                event.getEventDate(),
                sanitize(event.getCity()),
                event.getBudget() == null
                        ? "Not specified"
                        : "₹" + event.getBudget().toPlainString(),
                event.getGuestCount() == null
                        ? "Not specified"
                        : event.getGuestCount(),
                valueOrDefault(event.getTheme(), "Not specified"),
                valueOrDefault(
                        request.getPreferences(),
                        "No additional preferences"
                ),
                productContext
        );
    }
    
    private static String buildProductContext(List<Product> products) {
        StringBuilder builder = new StringBuilder();

        for (Product product : products) {
            builder.append("""
                    Product ID: %d
                    Name: %s
                    Category: %s
                    Price: ₹%s
                    Discount price: %s
                    Description: %s
                    Available quantity: %s
                    Featured: %s
                    ---
                    """.formatted(
                    product.getId(),
                    sanitize(product.getName()),
                    product.getCategory() == null
                            ? "Not specified"
                            : sanitize(product.getCategory().getName()),
                    product.getPrice().toPlainString(),
                    product.getDiscountPrice() == null
                            ? "Not available"
                            : "₹" + product.getDiscountPrice().toPlainString(),
                    valueOrDefault(
                            product.getShortDescription(),
                            "No description"
                    ),
                    product.getQuantity() == null
                            ? "Not specified"
                            : product.getQuantity(),
                    product.getFeatured()
            ));
        }

        return builder.toString();
    }

    private static String buildServiceContext(
            List<ServiceOffering> services
    ) {
        StringBuilder builder = new StringBuilder();

        for (ServiceOffering service : services) {
            builder.append("""
                    Service ID: %d
                    Name: %s
                    Vendor: %s
                    Type: %s
                    Category: %s
                    Base price: ₹%s
                    City: %s
                    Location type: %s
                    Duration minutes: %s
                    Rating: %s
                    Applicable event types: %s
                    Description: %s
                    ---
                    """.formatted(
                    service.getId(),
                    sanitize(service.getName()),
                    service.getVendor() == null
                            ? "Not specified"
                            : sanitize(
                                    service.getVendor().getBusinessName()
                            ),
                    service.getServiceType(),
                    service.getCategory() == null
                            ? "Not specified"
                            : sanitize(service.getCategory().getName()),
                    service.getBasePrice().toPlainString(),
                    sanitize(service.getCity()),
                    service.getLocationType(),
                    service.getDurationMinutes() == null
                            ? "Not specified"
                            : service.getDurationMinutes(),
                    service.getRating(),
                    service.getApplicableEventTypes(),
                    valueOrDefault(
                            service.getShortDescription(),
                            "No description"
                    )
            ));
        }

        return builder.toString();
    }

    public static String serviceRecommendations(
            Event event,
            EventRecommendationRequest request,
            List<ServiceOffering> services
    ) {
        String serviceContext = services.isEmpty()
                ? "No matching services were found."
                : buildServiceContext(services);

        return """
                You are an Indian event-services recommendation assistant.

                Recommend services only from the supplied marketplace data.
                Never invent vendors, services, prices, IDs or availability.

                Event details:
                Event ID: %d
                Event type: %s
                Title: %s
                Date: %s
                City: %s
                Budget: %s
                Guest count: %s
                Theme: %s

                Customer preferences:
                %s

                Available marketplace services:
                %s

                Instructions:
                - Recommend only relevant services from the supplied list.
                - Mention the exact service ID, service name, vendor and base price.
                - Prioritize services in the same city as the event.
                - Explain why each service fits the event.
                - Respect the maximum price when supplied.
                - Do not claim availability is confirmed.
                - If none are suitable, clearly say so.
                """.formatted(
                event.getId(),
                event.getEventType(),
                sanitize(event.getTitle()),
                event.getEventDate(),
                sanitize(event.getCity()),
                event.getBudget() == null
                        ? "Not specified"
                        : "₹" + event.getBudget().toPlainString(),
                event.getGuestCount() == null
                        ? "Not specified"
                        : event.getGuestCount(),
                valueOrDefault(event.getTheme(), "Not specified"),
                valueOrDefault(
                        request.getPreferences(),
                        "No additional preferences"
                ),
                serviceContext
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