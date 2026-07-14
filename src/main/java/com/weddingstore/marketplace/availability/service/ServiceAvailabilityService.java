package com.weddingstore.marketplace.availability.service;

import com.weddingstore.marketplace.availability.dto.*;

import java.util.List;

public interface ServiceAvailabilityService {

    AvailabilityResponse createAvailability(
            String userEmail,
            Long serviceId,
            CreateAvailabilityRequest request
    );

    AvailabilityResponse updateAvailability(
            String userEmail,
            Long serviceId,
            Long availabilityId,
            UpdateAvailabilityRequest request
    );

    void deleteAvailability(
            String userEmail,
            Long serviceId,
            Long availabilityId
    );

    List<AvailabilityResponse> getAvailability(
            Long serviceId
    );

    BlockedDateResponse addBlockedDate(
            String userEmail,
            Long serviceId,
            CreateBlockedDateRequest request
    );

    void deleteBlockedDate(
            String userEmail,
            Long serviceId,
            Long blockedDateId
    );

    List<BlockedDateResponse> getBlockedDates(
            Long serviceId
    );
}