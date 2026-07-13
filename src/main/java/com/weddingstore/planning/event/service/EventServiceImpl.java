package com.weddingstore.planning.event.service;

import com.weddingstore.common.exception.ResourceNotFoundException;
import com.weddingstore.planning.event.dto.CreateEventRequest;
import com.weddingstore.planning.event.dto.EventResponse;
import com.weddingstore.planning.event.dto.UpdateEventRequest;
import com.weddingstore.planning.event.entity.Event;
import com.weddingstore.planning.event.entity.EventStatus;
import com.weddingstore.planning.event.mapper.EventMapper;
import com.weddingstore.planning.event.repository.EventRepository;
import com.weddingstore.user.entity.User;
import com.weddingstore.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    public EventServiceImpl(
            EventRepository eventRepository,
            UserRepository userRepository,
            EventMapper eventMapper
    ) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public EventResponse create(
            String userEmail,
            CreateEventRequest request
    ) {
        User user = findUserByEmail(userEmail);

        Event event = Event.builder()
                .user(user)
                .eventType(request.getEventType())
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .eventDate(request.getEventDate())
                .city(request.getCity().trim())
                .venue(request.getVenue())
                .budget(request.getBudget())
                .guestCount(request.getGuestCount())
                .theme(request.getTheme())
                .imageUrl(request.getImageUrl())
                .status(
                        request.getStatus() == null
                                ? EventStatus.DRAFT
                                : request.getStatus()
                )
                .active(true)
                .build();

        Event savedEvent = eventRepository.save(event);
        return eventMapper.toResponse(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> getMyEvents(String userEmail) {
        User user = findUserByEmail(userEmail);

        return eventRepository
                .findByUserIdAndActiveTrueOrderByEventDateAsc(user.getId())
                .stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse getMyEventById(
            String userEmail,
            Long eventId
    ) {
        User user = findUserByEmail(userEmail);
        Event event = findOwnedEvent(eventId, user.getId());

        return eventMapper.toResponse(event);
    }

    @Override
    public EventResponse update(
            String userEmail,
            Long eventId,
            UpdateEventRequest request
    ) {
        User user = findUserByEmail(userEmail);
        Event event = findOwnedEvent(eventId, user.getId());

        event.setEventType(request.getEventType());
        event.setTitle(request.getTitle().trim());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setCity(request.getCity().trim());
        event.setVenue(request.getVenue());
        event.setBudget(request.getBudget());
        event.setGuestCount(request.getGuestCount());
        event.setTheme(request.getTheme());
        event.setImageUrl(request.getImageUrl());
        event.setStatus(request.getStatus());

        Event updatedEvent = eventRepository.save(event);
        return eventMapper.toResponse(updatedEvent);
    }

    @Override
    public void delete(String userEmail, Long eventId) {
        User user = findUserByEmail(userEmail);
        Event event = findOwnedEvent(eventId, user.getId());

        event.setActive(false);
        event.setStatus(EventStatus.CANCELLED);

        eventRepository.save(event);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    private Event findOwnedEvent(Long eventId, Long userId) {
        return eventRepository
                .findByIdAndUserIdAndActiveTrue(eventId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found or access denied"
                        )
                );
    }
}