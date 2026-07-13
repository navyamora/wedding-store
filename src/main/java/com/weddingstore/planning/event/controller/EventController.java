package com.weddingstore.planning.event.controller;

import com.weddingstore.common.response.ApiResponse;
import com.weddingstore.planning.event.dto.CreateEventRequest;
import com.weddingstore.planning.event.dto.EventResponse;
import com.weddingstore.planning.event.dto.UpdateEventRequest;
import com.weddingstore.planning.event.service.EventService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ApiResponse<EventResponse> create(
            Authentication authentication,
            @Valid @RequestBody CreateEventRequest request
    ) {
        EventResponse event = eventService.create(
                authentication.getName(),
                request
        );

        return ApiResponse.<EventResponse>builder()
                .success(true)
                .message("Event created successfully")
                .data(event)
                .build();
    }

    @GetMapping
    public ApiResponse<List<EventResponse>> getMyEvents(
            Authentication authentication
    ) {
        List<EventResponse> events =
                eventService.getMyEvents(authentication.getName());

        return ApiResponse.<List<EventResponse>>builder()
                .success(true)
                .message("Events fetched successfully")
                .data(events)
                .build();
    }

    @GetMapping("/{eventId}")
    public ApiResponse<EventResponse> getById(
            Authentication authentication,
            @PathVariable Long eventId
    ) {
        EventResponse event = eventService.getMyEventById(
                authentication.getName(),
                eventId
        );

        return ApiResponse.<EventResponse>builder()
                .success(true)
                .message("Event fetched successfully")
                .data(event)
                .build();
    }

    @PutMapping("/{eventId}")
    public ApiResponse<EventResponse> update(
            Authentication authentication,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventRequest request
    ) {
        EventResponse event = eventService.update(
                authentication.getName(),
                eventId,
                request
        );

        return ApiResponse.<EventResponse>builder()
                .success(true)
                .message("Event updated successfully")
                .data(event)
                .build();
    }

    @DeleteMapping("/{eventId}")
    public ApiResponse<Object> delete(
            Authentication authentication,
            @PathVariable Long eventId
    ) {
        eventService.delete(authentication.getName(), eventId);

        return ApiResponse.builder()
                .success(true)
                .message("Event deleted successfully")
                .data(null)
                .build();
    }
}