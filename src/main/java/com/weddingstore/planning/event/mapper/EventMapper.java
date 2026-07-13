package com.weddingstore.planning.event.mapper;

import com.weddingstore.planning.event.dto.EventResponse;
import com.weddingstore.planning.event.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    @Mapping(source = "user.id", target = "userId")
    EventResponse toResponse(Event event);
}