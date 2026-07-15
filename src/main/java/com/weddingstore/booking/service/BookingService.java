package com.weddingstore.booking.service;

import com.weddingstore.booking.dto.BookingResponse;
import com.weddingstore.booking.dto.CreateBookingRequest;
import com.weddingstore.booking.dto.UpdateBookingStatusRequest;

import java.util.List;

public interface BookingService {

    BookingResponse create(
            String userEmail,
            CreateBookingRequest request
    );

    List<BookingResponse> getMyBookings(String userEmail);

    BookingResponse cancel(
            String userEmail,
            Long bookingId
    );

    List<BookingResponse> getVendorBookings(
            String userEmail
    );

    BookingResponse getVendorBooking(
            String userEmail,
            Long bookingId
    );

    BookingResponse updateVendorStatus(
            String userEmail,
            Long bookingId,
            UpdateBookingStatusRequest request
    );
}