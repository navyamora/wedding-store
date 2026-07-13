package com.weddingstore.planning.event.service;

import com.weddingstore.planning.event.dto.CreateEventRequest;
import com.weddingstore.planning.event.dto.EventResponse;
import com.weddingstore.planning.event.dto.UpdateEventRequest;

import java.util.List;

public interface EventService {

    EventResponse create(String userEmail, CreateEventRequest request);

    List<EventResponse> getMyEvents(String userEmail);

    EventResponse getMyEventById(String userEmail, Long eventId);

    EventResponse update(
            String userEmail,
            Long eventId,
            UpdateEventRequest request
    );

    void delete(String userEmail, Long eventId);
}