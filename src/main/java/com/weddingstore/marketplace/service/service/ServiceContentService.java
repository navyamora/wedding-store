package com.weddingstore.marketplace.service.service;

import com.weddingstore.marketplace.service.dto.*;

import java.util.List;

public interface ServiceContentService {

    ServiceImageResponse addImage(
            String userEmail,
            Long serviceId,
            AddServiceImageRequest request
    );

    List<ServiceImageResponse> getImages(Long serviceId);

    void deleteImage(
            String userEmail,
            Long serviceId,
            Long imageId
    );

    ServicePackageResponse createPackage(
            String userEmail,
            Long serviceId,
            CreateServicePackageRequest request
    );

    List<ServicePackageResponse> getPackages(Long serviceId);

    ServicePackageResponse updatePackage(
            String userEmail,
            Long serviceId,
            Long packageId,
            UpdateServicePackageRequest request
    );

    void deletePackage(
            String userEmail,
            Long serviceId,
            Long packageId
    );
}