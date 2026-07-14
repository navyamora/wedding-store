package com.weddingstore.marketplace.availability.service;

import com.weddingstore.common.exception.ConflictException;
import com.weddingstore.common.exception.ResourceNotFoundException;
import com.weddingstore.marketplace.availability.dto.*;
import com.weddingstore.marketplace.availability.entity.ServiceAvailability;
import com.weddingstore.marketplace.availability.entity.ServiceBlockedDate;
import com.weddingstore.marketplace.availability.mapper.AvailabilityMapper;
import com.weddingstore.marketplace.availability.repository.ServiceAvailabilityRepository;
import com.weddingstore.marketplace.availability.repository.ServiceBlockedDateRepository;
import com.weddingstore.marketplace.service.entity.ServiceOffering;
import com.weddingstore.marketplace.service.repository.ServiceOfferingRepository;
import com.weddingstore.marketplace.vendor.entity.VendorProfile;
import com.weddingstore.marketplace.vendor.entity.VendorStatus;
import com.weddingstore.marketplace.vendor.repository.VendorProfileRepository;
import com.weddingstore.user.entity.User;
import com.weddingstore.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ServiceAvailabilityServiceImpl
        implements ServiceAvailabilityService {

    private final ServiceAvailabilityRepository availabilityRepository;
    private final ServiceBlockedDateRepository blockedDateRepository;
    private final ServiceOfferingRepository serviceRepository;
    private final VendorProfileRepository vendorRepository;
    private final UserRepository userRepository;
    private final AvailabilityMapper mapper;

    public ServiceAvailabilityServiceImpl(
            ServiceAvailabilityRepository availabilityRepository,
            ServiceBlockedDateRepository blockedDateRepository,
            ServiceOfferingRepository serviceRepository,
            VendorProfileRepository vendorRepository,
            UserRepository userRepository,
            AvailabilityMapper mapper
    ) {
        this.availabilityRepository = availabilityRepository;
        this.blockedDateRepository = blockedDateRepository;
        this.serviceRepository = serviceRepository;
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public AvailabilityResponse createAvailability(
            String userEmail,
            Long serviceId,
            CreateAvailabilityRequest request
    ) {
        ServiceOffering service = findOwnedService(
                userEmail,
                serviceId
        );

        validateTimeRange(
                request.getStartTime(),
                request.getEndTime(),
                request.getSlotDurationMinutes()
        );

        ServiceAvailability availability =
                ServiceAvailability.builder()
                        .service(service)
                        .dayOfWeek(request.getDayOfWeek())
                        .startTime(request.getStartTime())
                        .endTime(request.getEndTime())
                        .slotDurationMinutes(
                                request.getSlotDurationMinutes()
                        )
                        .active(true)
                        .build();

        return mapper.toResponse(
                availabilityRepository.save(availability)
        );
    }

    @Override
    public AvailabilityResponse updateAvailability(
            String userEmail,
            Long serviceId,
            Long availabilityId,
            UpdateAvailabilityRequest request
    ) {
        findOwnedService(userEmail, serviceId);

        validateTimeRange(
                request.getStartTime(),
                request.getEndTime(),
                request.getSlotDurationMinutes()
        );

        ServiceAvailability availability =
                availabilityRepository
                        .findByIdAndServiceIdAndActiveTrue(
                                availabilityId,
                                serviceId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Availability record not found"
                                )
                        );

        availability.setDayOfWeek(request.getDayOfWeek());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setSlotDurationMinutes(
                request.getSlotDurationMinutes()
        );

        if (request.getActive() != null) {
            availability.setActive(request.getActive());
        }

        return mapper.toResponse(
                availabilityRepository.save(availability)
        );
    }

    @Override
    public void deleteAvailability(
            String userEmail,
            Long serviceId,
            Long availabilityId
    ) {
        findOwnedService(userEmail, serviceId);

        ServiceAvailability availability =
                availabilityRepository
                        .findByIdAndServiceIdAndActiveTrue(
                                availabilityId,
                                serviceId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Availability record not found"
                                )
                        );

        availability.setActive(false);
        availabilityRepository.save(availability);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponse> getAvailability(
            Long serviceId
    ) {
        findPublicService(serviceId);

        return availabilityRepository
                .findByServiceIdAndActiveTrueOrderByDayOfWeekAscStartTimeAsc(
                        serviceId
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public BlockedDateResponse addBlockedDate(
            String userEmail,
            Long serviceId,
            CreateBlockedDateRequest request
    ) {
        ServiceOffering service = findOwnedService(
                userEmail,
                serviceId
        );

        if (blockedDateRepository
                .existsByServiceIdAndBlockedDateAndActiveTrue(
                        serviceId,
                        request.getBlockedDate()
                )) {
            throw new ConflictException(
                    "This date is already blocked"
            );
        }

        ServiceBlockedDate blockedDate =
                ServiceBlockedDate.builder()
                        .service(service)
                        .blockedDate(request.getBlockedDate())
                        .reason(normalize(request.getReason()))
                        .active(true)
                        .build();

        return mapper.toResponse(
                blockedDateRepository.save(blockedDate)
        );
    }

    @Override
    public void deleteBlockedDate(
            String userEmail,
            Long serviceId,
            Long blockedDateId
    ) {
        findOwnedService(userEmail, serviceId);

        ServiceBlockedDate blockedDate =
                blockedDateRepository
                        .findByIdAndServiceIdAndActiveTrue(
                                blockedDateId,
                                serviceId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Blocked date not found"
                                )
                        );

        blockedDate.setActive(false);
        blockedDateRepository.save(blockedDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlockedDateResponse> getBlockedDates(
            Long serviceId
    ) {
        findPublicService(serviceId);

        return blockedDateRepository
                .findByServiceIdAndActiveTrueAndBlockedDateGreaterThanEqualOrderByBlockedDateAsc(
                        serviceId,
                        LocalDate.now()
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    private ServiceOffering findOwnedService(
            String userEmail,
            Long serviceId
    ) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        VendorProfile vendor = vendorRepository
                .findByUserIdAndActiveTrue(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vendor profile not found"
                        )
                );

        if (vendor.getStatus() != VendorStatus.APPROVED) {
            throw new ConflictException(
                    "Vendor must be approved"
            );
        }

        return serviceRepository
                .findByIdAndVendorIdAndActiveTrue(
                        serviceId,
                        vendor.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service not found or access denied"
                        )
                );
    }

    private ServiceOffering findPublicService(Long serviceId) {
        return serviceRepository
                .findByIdAndActiveTrue(serviceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service not found"
                        )
                );
    }

    private void validateTimeRange(
            java.time.LocalTime startTime,
            java.time.LocalTime endTime,
            Integer slotDurationMinutes
    ) {
        if (!startTime.isBefore(endTime)) {
            throw new ConflictException(
                    "Start time must be before end time"
            );
        }

        long availableMinutes =
                java.time.Duration
                        .between(startTime, endTime)
                        .toMinutes();

        if (slotDurationMinutes > availableMinutes) {
            throw new ConflictException(
                    "Slot duration cannot exceed the available time range"
            );
        }
    }

    private String normalize(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }
}