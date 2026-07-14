package com.weddingstore.marketplace.availability.mapper;

import com.weddingstore.marketplace.availability.dto.AvailabilityResponse;
import com.weddingstore.marketplace.availability.dto.BlockedDateResponse;
import com.weddingstore.marketplace.availability.entity.ServiceAvailability;
import com.weddingstore.marketplace.availability.entity.ServiceBlockedDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AvailabilityMapper {

    @Mapping(source = "service.id", target = "serviceId")
    AvailabilityResponse toResponse(
            ServiceAvailability availability
    );

    @Mapping(source = "service.id", target = "serviceId")
    BlockedDateResponse toResponse(
            ServiceBlockedDate blockedDate
    );
}