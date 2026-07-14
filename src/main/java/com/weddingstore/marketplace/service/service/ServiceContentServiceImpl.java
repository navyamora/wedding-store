package com.weddingstore.marketplace.service.service;

import com.weddingstore.common.exception.ConflictException;
import com.weddingstore.common.exception.ResourceNotFoundException;
import com.weddingstore.marketplace.service.dto.*;
import com.weddingstore.marketplace.service.entity.ServiceImage;
import com.weddingstore.marketplace.service.entity.ServiceOffering;
import com.weddingstore.marketplace.service.entity.ServicePackage;
import com.weddingstore.marketplace.service.mapper.ServiceOfferingMapper;
import com.weddingstore.marketplace.service.repository.*;
import com.weddingstore.marketplace.vendor.entity.VendorProfile;
import com.weddingstore.marketplace.vendor.entity.VendorStatus;
import com.weddingstore.marketplace.vendor.repository.VendorProfileRepository;
import com.weddingstore.user.entity.User;
import com.weddingstore.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServiceContentServiceImpl implements ServiceContentService {

    private static final long MAX_IMAGES_PER_SERVICE = 10;

    private final ServiceOfferingRepository serviceRepository;
    private final ServiceImageRepository imageRepository;
    private final ServicePackageRepository packageRepository;
    private final VendorProfileRepository vendorRepository;
    private final UserRepository userRepository;
    private final ServiceOfferingMapper mapper;

    public ServiceContentServiceImpl(
            ServiceOfferingRepository serviceRepository,
            ServiceImageRepository imageRepository,
            ServicePackageRepository packageRepository,
            VendorProfileRepository vendorRepository,
            UserRepository userRepository,
            ServiceOfferingMapper mapper
    ) {
        this.serviceRepository = serviceRepository;
        this.imageRepository = imageRepository;
        this.packageRepository = packageRepository;
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public ServiceImageResponse addImage(
            String userEmail,
            Long serviceId,
            AddServiceImageRequest request
    ) {
        ServiceOffering service = findOwnedService(userEmail, serviceId);

        if (imageRepository.countByServiceId(serviceId)
                >= MAX_IMAGES_PER_SERVICE) {
            throw new ConflictException(
                    "A service can have a maximum of 10 images"
            );
        }

        boolean makePrimary =
                Boolean.TRUE.equals(request.getPrimaryImage());

        if (makePrimary) {
            service.getImages().forEach(
                    image -> image.setPrimaryImage(false)
            );
        }

        ServiceImage image = ServiceImage.builder()
                .service(service)
                .imageUrl(request.getImageUrl().trim())
                .displayOrder(request.getDisplayOrder())
                .primaryImage(makePrimary)
                .altText(normalize(request.getAltText()))
                .build();

        ServiceImage saved = imageRepository.save(image);

        return mapper.toImageResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceImageResponse> getImages(Long serviceId) {
        findPublicService(serviceId);

        return imageRepository
                .findByServiceIdOrderByDisplayOrderAsc(serviceId)
                .stream()
                .map(mapper::toImageResponse)
                .toList();
    }

    @Override
    public void deleteImage(
            String userEmail,
            Long serviceId,
            Long imageId
    ) {
        findOwnedService(userEmail, serviceId);

        ServiceImage image = imageRepository
                .findByIdAndServiceId(imageId, serviceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service image not found"
                        )
                );

        imageRepository.delete(image);
    }

    @Override
    public ServicePackageResponse createPackage(
            String userEmail,
            Long serviceId,
            CreateServicePackageRequest request
    ) {
        ServiceOffering service = findOwnedService(userEmail, serviceId);

        ServicePackage servicePackage = ServicePackage.builder()
                .service(service)
                .packageName(request.getPackageName().trim())
                .description(normalize(request.getDescription()))
                .price(request.getPrice())
                .durationMinutes(request.getDurationMinutes())
                .inclusions(normalize(request.getInclusions()))
                .exclusions(normalize(request.getExclusions()))
                .active(true)
                .displayOrder(request.getDisplayOrder())
                .build();

        return mapper.toPackageResponse(
                packageRepository.save(servicePackage)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicePackageResponse> getPackages(Long serviceId) {
        findPublicService(serviceId);

        return packageRepository
                .findByServiceIdAndActiveTrueOrderByDisplayOrderAsc(
                        serviceId
                )
                .stream()
                .map(mapper::toPackageResponse)
                .toList();
    }

    @Override
    public ServicePackageResponse updatePackage(
            String userEmail,
            Long serviceId,
            Long packageId,
            UpdateServicePackageRequest request
    ) {
        findOwnedService(userEmail, serviceId);

        ServicePackage servicePackage = packageRepository
                .findByIdAndServiceIdAndActiveTrue(
                        packageId,
                        serviceId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service package not found"
                        )
                );

        servicePackage.setPackageName(
                request.getPackageName().trim()
        );
        servicePackage.setDescription(
                normalize(request.getDescription())
        );
        servicePackage.setPrice(request.getPrice());
        servicePackage.setDurationMinutes(
                request.getDurationMinutes()
        );
        servicePackage.setInclusions(
                normalize(request.getInclusions())
        );
        servicePackage.setExclusions(
                normalize(request.getExclusions())
        );
        servicePackage.setDisplayOrder(
                request.getDisplayOrder()
        );

        if (request.getActive() != null) {
            servicePackage.setActive(request.getActive());
        }

        return mapper.toPackageResponse(
                packageRepository.save(servicePackage)
        );
    }

    @Override
    public void deletePackage(
            String userEmail,
            Long serviceId,
            Long packageId
    ) {
        findOwnedService(userEmail, serviceId);

        ServicePackage servicePackage = packageRepository
                .findByIdAndServiceIdAndActiveTrue(
                        packageId,
                        serviceId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service package not found"
                        )
                );

        servicePackage.setActive(false);
        packageRepository.save(servicePackage);
    }

    private ServiceOffering findOwnedService(
            String userEmail,
            Long serviceId
    ) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
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
        return serviceRepository.findByIdAndActiveTrue(serviceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service not found"
                        )
                );
    }

    private String normalize(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }
}