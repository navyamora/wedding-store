package com.weddingstore.ai.service;

import com.weddingstore.ai.dto.AiEventResponse;
import com.weddingstore.ai.prompt.PromptBuilder;
import com.weddingstore.common.exception.ResourceNotFoundException;
import com.weddingstore.planning.event.entity.Event;
import com.weddingstore.planning.event.repository.EventRepository;
import com.weddingstore.user.entity.User;
import com.weddingstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiEventServiceImpl implements AiEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final AiService aiService;

    @Override
    public AiEventResponse generateBudgetPlan(
            String userEmail,
            Long eventId
    ) {
        Event event = findOwnedEvent(userEmail, eventId);

        if (event.getBudget() == null) {
            throw new IllegalArgumentException(
                    "Add a budget to the event before generating a budget plan"
            );
        }

        String result = aiService.generateFromPrompt(
                PromptBuilder.eventBudgetPlan(event)
        );

        return AiEventResponse.builder()
                .eventId(event.getId())
                .eventTitle(event.getTitle())
                .result(result)
                .build();
    }

    @Override
    public AiEventResponse generateChecklist(
            String userEmail,
            Long eventId
    ) {
        Event event = findOwnedEvent(userEmail, eventId);

        String result = aiService.generateFromPrompt(
                PromptBuilder.eventChecklist(event)
        );

        return AiEventResponse.builder()
                .eventId(event.getId())
                .eventTitle(event.getTitle())
                .result(result)
                .build();
    }

    private Event findOwnedEvent(
            String userEmail,
            Long eventId
    ) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        return eventRepository
                .findByIdAndUserIdAndActiveTrue(
                        eventId,
                        user.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found or access denied"
                        )
                );
    }
}