package com.weddingstore.booking.mapper;

import com.weddingstore.booking.dto.BookingResponse;
import com.weddingstore.booking.entity.ServiceBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(
            expression = "java(booking.getCustomer().getFirstName() + \" \" + booking.getCustomer().getLastName())",
            target = "customerName"
    )
    @Mapping(source = "customer.email", target = "customerEmail")

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.title", target = "eventTitle")
    @Mapping(
            expression = "java(booking.getEvent().getEventType().name())",
            target = "eventType"
    )

    @Mapping(source = "service.id", target = "serviceId")
    @Mapping(source = "service.name", target = "serviceName")

    @Mapping(source = "service.vendor.id", target = "vendorId")
    @Mapping(
            source = "service.vendor.businessName",
            target = "vendorBusinessName"
    )

    @Mapping(source = "servicePackage.id", target = "packageId")
    @Mapping(
            source = "servicePackage.packageName",
            target = "packageName"
    )
    BookingResponse toResponse(ServiceBooking booking);
}