package com.weddingstore.marketplace.service.service;

import com.weddingstore.marketplace.service.dto.CreateServiceRequest;
import com.weddingstore.marketplace.service.dto.ServiceResponse;
import com.weddingstore.marketplace.service.dto.UpdateServiceRequest;
import com.weddingstore.marketplace.service.entity.ServiceType;

import java.math.BigDecimal;
import java.util.List;

public interface ServiceOfferingService {

    ServiceResponse create(
            String userEmail,
            CreateServiceRequest request
    );

    List<ServiceResponse> getMyServices(String userEmail);

    ServiceResponse getMyService(
            String userEmail,
            Long serviceId
    );

    ServiceResponse update(
            String userEmail,
            Long serviceId,
            UpdateServiceRequest request
    );

    void delete(
            String userEmail,
            Long serviceId
    );

    List<ServiceResponse> searchPublic(
            String city,
            ServiceType serviceType,
            Long categoryId,
            BigDecimal maxPrice,
            Boolean featured
    );

    ServiceResponse getPublicById(Long serviceId);
}