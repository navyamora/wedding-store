package com.weddingstore.planning.event.repository;

import com.weddingstore.planning.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByUserIdAndActiveTrueOrderByEventDateAsc(Long userId);

    Optional<Event> findByIdAndUserIdAndActiveTrue(Long id, Long userId);

    boolean existsByIdAndUserIdAndActiveTrue(Long id, Long userId);
}