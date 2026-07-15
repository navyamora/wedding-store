package com.weddingstore.booking.service;

import com.weddingstore.booking.dto.BookingResponse;
import com.weddingstore.booking.dto.CreateBookingRequest;
import com.weddingstore.booking.dto.UpdateBookingStatusRequest;
import com.weddingstore.booking.entity.BookingStatus;
import com.weddingstore.booking.entity.ServiceBooking;
import com.weddingstore.booking.mapper.BookingMapper;
import com.weddingstore.booking.repository.ServiceBookingRepository;
import com.weddingstore.common.exception.ConflictException;
import com.weddingstore.common.exception.ResourceNotFoundException;
import com.weddingstore.marketplace.availability.entity.ServiceAvailability;
import com.weddingstore.marketplace.availability.repository.ServiceAvailabilityRepository;
import com.weddingstore.marketplace.availability.repository.ServiceBlockedDateRepository;
import com.weddingstore.marketplace.service.entity.ServiceOffering;
import com.weddingstore.marketplace.service.entity.ServicePackage;
import com.weddingstore.marketplace.service.repository.ServiceOfferingRepository;
import com.weddingstore.marketplace.service.repository.ServicePackageRepository;
import com.weddingstore.marketplace.vendor.entity.VendorProfile;
import com.weddingstore.marketplace.vendor.entity.VendorStatus;
import com.weddingstore.marketplace.vendor.repository.VendorProfileRepository;
import com.weddingstore.planning.event.entity.Event;
import com.weddingstore.planning.event.repository.EventRepository;
import com.weddingstore.user.entity.User;
import com.weddingstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private static final Set<BookingStatus> RESERVED_STATUSES =
            Set.of(
                    BookingStatus.ACCEPTED,
                    BookingStatus.CONFIRMED,
                    BookingStatus.IN_PROGRESS
            );

    private final ServiceBookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ServiceOfferingRepository serviceRepository;
    private final ServicePackageRepository packageRepository;
    private final VendorProfileRepository vendorRepository;
    private final ServiceAvailabilityRepository availabilityRepository;
    private final ServiceBlockedDateRepository blockedDateRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponse create(
            String userEmail,
            CreateBookingRequest request
    ) {
        User customer = findUser(userEmail);

        Event event = eventRepository
                .findByIdAndUserIdAndActiveTrue(
                        request.getEventId(),
                        customer.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found or access denied"
                        )
                );

        ServiceOffering service = serviceRepository
                .findByIdAndActiveTrue(request.getServiceId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service not found"
                        )
                );

        validateVendor(service.getVendor());
        validateEventType(service, event);
        validateBlockedDate(service, request);
        validateAvailability(service, request);

        ServicePackage servicePackage =
                resolvePackage(service, request.getPackageId());

        BigDecimal amount = servicePackage == null
                ? service.getBasePrice()
                : servicePackage.getPrice();

        int durationMinutes = servicePackage != null
                && servicePackage.getDurationMinutes() != null
                ? servicePackage.getDurationMinutes()
                : resolveDuration(service);

        LocalTime endTime = request.getStartTime()
                .plusMinutes(durationMinutes);

        boolean alreadyReserved = bookingRepository
                .existsByServiceIdAndRequestedDateAndStartTimeAndStatusIn(
                        service.getId(),
                        request.getRequestedDate(),
                        request.getStartTime(),
                        RESERVED_STATUSES
                );

        if (alreadyReserved) {
            throw new ConflictException(
                    "The requested service slot is no longer available"
            );
        }

        ServiceBooking booking = ServiceBooking.builder()
                .customer(customer)
                .event(event)
                .service(service)
                .servicePackage(servicePackage)
                .requestedDate(request.getRequestedDate())
                .startTime(request.getStartTime())
                .endTime(endTime)
                .location(request.getLocation().trim())
                .customerNotes(normalize(
                        request.getCustomerNotes()
                ))
                .estimatedAmount(amount)
                .status(BookingStatus.REQUESTED)
                .active(true)
                .build();

        return bookingMapper.toResponse(
                bookingRepository.save(booking)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(String userEmail) {
        User customer = findUser(userEmail);

        return bookingRepository
                .findByCustomerIdAndActiveTrueOrderByCreatedAtDesc(
                        customer.getId()
                )
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public BookingResponse cancel(
            String userEmail,
            Long bookingId
    ) {
        User customer = findUser(userEmail);

        ServiceBooking booking = bookingRepository
                .findByIdAndCustomerIdAndActiveTrue(
                        bookingId,
                        customer.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found or access denied"
                        )
                );

        if (booking.getStatus() == BookingStatus.COMPLETED
                || booking.getStatus() == BookingStatus.REJECTED
                || booking.getStatus() == BookingStatus.CANCELLED) {

            throw new ConflictException(
                    "This booking cannot be cancelled"
            );
        }

        booking.setStatus(BookingStatus.CANCELLED);

        return bookingMapper.toResponse(
                bookingRepository.save(booking)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getVendorBookings(
            String userEmail
    ) {
        VendorProfile vendor = findVendor(userEmail);

        return bookingRepository
                .findByServiceVendorIdAndActiveTrueOrderByCreatedAtDesc(
                        vendor.getId()
                )
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getVendorBooking(
            String userEmail,
            Long bookingId
    ) {
        VendorProfile vendor = findVendor(userEmail);

        ServiceBooking booking = bookingRepository
                .findByIdAndServiceVendorIdAndActiveTrue(
                        bookingId,
                        vendor.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found or access denied"
                        )
                );

        return bookingMapper.toResponse(booking);
    }

    @Override
    public BookingResponse updateVendorStatus(
            String userEmail,
            Long bookingId,
            UpdateBookingStatusRequest request
    ) {
        VendorProfile vendor = findVendor(userEmail);

        ServiceBooking booking = bookingRepository
                .findByIdAndServiceVendorIdAndActiveTrue(
                        bookingId,
                        vendor.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found or access denied"
                        )
                );

        validateVendorStatusTransition(
                booking.getStatus(),
                request.getStatus()
        );

        booking.setStatus(request.getStatus());
        booking.setVendorMessage(normalize(
                request.getVendorMessage()
        ));

        return bookingMapper.toResponse(
                bookingRepository.save(booking)
        );
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );
    }

    private VendorProfile findVendor(String email) {
        User user = findUser(email);

        VendorProfile vendor = vendorRepository
                .findByUserIdAndActiveTrue(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vendor profile not found"
                        )
                );

        validateVendor(vendor);
        return vendor;
    }

    private void validateVendor(VendorProfile vendor) {
        if (vendor.getStatus() != VendorStatus.APPROVED
                || !Boolean.TRUE.equals(vendor.getActive())) {

            throw new ConflictException(
                    "Vendor is not available for bookings"
            );
        }
    }

    private void validateEventType(
            ServiceOffering service,
            Event event
    ) {
        if (service.getApplicableEventTypes() != null
                && !service.getApplicableEventTypes().isEmpty()
                && !service.getApplicableEventTypes().contains(
                        event.getEventType()
                )) {

            throw new ConflictException(
                    "This service does not support the selected event type"
            );
        }
    }

    private void validateBlockedDate(
            ServiceOffering service,
            CreateBookingRequest request
    ) {
        boolean blocked = blockedDateRepository
                .existsByServiceIdAndBlockedDateAndActiveTrue(
                        service.getId(),
                        request.getRequestedDate()
                );

        if (blocked) {
            throw new ConflictException(
                    "The vendor is unavailable on the requested date"
            );
        }
    }

    private void validateAvailability(
            ServiceOffering service,
            CreateBookingRequest request
    ) {
        List<ServiceAvailability> schedules =
                availabilityRepository
                        .findByServiceIdAndDayOfWeekAndActiveTrueOrderByStartTimeAsc(
                                service.getId(),
                                request.getRequestedDate().getDayOfWeek()
                        );

        if (schedules.isEmpty()) {
            throw new ConflictException(
                    "The service is not available on this day"
            );
        }

        boolean insideAvailableWindow = schedules.stream()
                .anyMatch(schedule ->
                        !request.getStartTime().isBefore(
                                schedule.getStartTime()
                        )
                        && request.getStartTime().isBefore(
                                schedule.getEndTime()
                        )
                );

        if (!insideAvailableWindow) {
            throw new ConflictException(
                    "The requested start time is outside vendor availability"
            );
        }
    }

    private ServicePackage resolvePackage(
            ServiceOffering service,
            Long packageId
    ) {
        if (packageId == null) {
            return null;
        }

        return packageRepository
                .findByIdAndServiceIdAndActiveTrue(
                        packageId,
                        service.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service package not found"
                        )
                );
    }

    private int resolveDuration(ServiceOffering service) {
        if (service.getDurationMinutes() != null) {
            return service.getDurationMinutes();
        }

        return 60;
    }

    private void validateVendorStatusTransition(
            BookingStatus current,
            BookingStatus requested
    ) {
        boolean valid = switch (current) {
            case REQUESTED ->
                    requested == BookingStatus.ACCEPTED
                    || requested == BookingStatus.REJECTED;

            case ACCEPTED ->
                    requested == BookingStatus.CONFIRMED
                    || requested == BookingStatus.REJECTED;

            case CONFIRMED ->
                    requested == BookingStatus.IN_PROGRESS
                    || requested == BookingStatus.CANCELLED;

            case IN_PROGRESS ->
                    requested == BookingStatus.COMPLETED;

            default -> false;
        };

        if (!valid) {
            throw new ConflictException(
                    "Invalid booking status transition from "
                            + current + " to " + requested
            );
        }
    }

    private String normalize(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }
}