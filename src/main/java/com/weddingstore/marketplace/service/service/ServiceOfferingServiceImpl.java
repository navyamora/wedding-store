package com.weddingstore.marketplace.service.service;

import com.weddingstore.common.exception.ConflictException;
import com.weddingstore.common.exception.ResourceNotFoundException;
import com.weddingstore.marketplace.category.entity.Category;
import com.weddingstore.marketplace.category.repository.CategoryRepository;
import com.weddingstore.marketplace.service.dto.CreateServiceRequest;
import com.weddingstore.marketplace.service.dto.ServiceResponse;
import com.weddingstore.marketplace.service.dto.UpdateServiceRequest;
import com.weddingstore.marketplace.service.entity.ServiceOffering;
import com.weddingstore.marketplace.service.entity.ServiceType;
import com.weddingstore.marketplace.service.mapper.ServiceOfferingMapper;
import com.weddingstore.marketplace.service.repository.ServiceOfferingRepository;
import com.weddingstore.marketplace.vendor.entity.VendorProfile;
import com.weddingstore.marketplace.vendor.entity.VendorStatus;
import com.weddingstore.marketplace.vendor.repository.VendorProfileRepository;
import com.weddingstore.user.entity.User;
import com.weddingstore.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class ServiceOfferingServiceImpl
        implements ServiceOfferingService {

    private final ServiceOfferingRepository serviceRepository;
    private final VendorProfileRepository vendorRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ServiceOfferingMapper serviceMapper;

    public ServiceOfferingServiceImpl(
            ServiceOfferingRepository serviceRepository,
            VendorProfileRepository vendorRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            ServiceOfferingMapper serviceMapper
    ) {
        this.serviceRepository = serviceRepository;
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.serviceMapper = serviceMapper;
    }

    @Override
    public ServiceResponse create(
            String userEmail,
            CreateServiceRequest request
    ) {
        VendorProfile vendor = findApprovedVendor(userEmail);
        Category category = findCategory(request.getCategoryId());

        String slug = generateUniqueSlug(request.getName());

        ServiceOffering service = ServiceOffering.builder()
                .vendor(vendor)
                .category(category)
                .serviceType(request.getServiceType())
                .name(request.getName().trim())
                .slug(slug)
                .shortDescription(
                        normalize(request.getShortDescription())
                )
                .description(normalize(request.getDescription()))
                .basePrice(request.getBasePrice())
                .durationMinutes(request.getDurationMinutes())
                .locationType(request.getLocationType())
                .city(request.getCity().trim())
                .state(request.getState().trim())
                .country(
                        valueOrDefault(
                                request.getCountry(),
                                "India"
                        )
                )
                .serviceRadiusKm(request.getServiceRadiusKm())
                .advancePercentage(
                        request.getAdvancePercentage()
                )
                .featured(
                        Boolean.TRUE.equals(request.getFeatured())
                )
                .active(true)
                .applicableEventTypes(
                        request.getApplicableEventTypes() == null
                                ? new HashSet<>()
                                : new HashSet<>(
                                        request.getApplicableEventTypes()
                                )
                )
                .build();

        return serviceMapper.toResponse(
                serviceRepository.save(service)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> getMyServices(String userEmail) {
        VendorProfile vendor = findVendor(userEmail);

        return serviceRepository
                .findByVendorIdAndActiveTrueOrderByCreatedAtDesc(
                        vendor.getId()
                )
                .stream()
                .map(serviceMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse getMyService(
            String userEmail,
            Long serviceId
    ) {
        VendorProfile vendor = findVendor(userEmail);

        ServiceOffering service = serviceRepository
                .findByIdAndVendorIdAndActiveTrue(
                        serviceId,
                        vendor.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service not found or access denied"
                        )
                );

        return serviceMapper.toResponse(service);
    }

    @Override
    public ServiceResponse update(
            String userEmail,
            Long serviceId,
            UpdateServiceRequest request
    ) {
        VendorProfile vendor = findApprovedVendor(userEmail);

        ServiceOffering service = serviceRepository
                .findByIdAndVendorIdAndActiveTrue(
                        serviceId,
                        vendor.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service not found or access denied"
                        )
                );

        Category category = findCategory(request.getCategoryId());
        String slug = generateSlug(request.getName());

        if (serviceRepository.existsBySlugAndIdNot(
                slug,
                serviceId
        )) {
            slug = slug + "-" + serviceId;
        }

        service.setCategory(category);
        service.setServiceType(request.getServiceType());
        service.setName(request.getName().trim());
        service.setSlug(slug);
        service.setShortDescription(
                normalize(request.getShortDescription())
        );
        service.setDescription(
                normalize(request.getDescription())
        );
        service.setBasePrice(request.getBasePrice());
        service.setDurationMinutes(
                request.getDurationMinutes()
        );
        service.setLocationType(request.getLocationType());
        service.setCity(request.getCity().trim());
        service.setState(request.getState().trim());
        service.setCountry(
                valueOrDefault(request.getCountry(), "India")
        );
        service.setServiceRadiusKm(
                request.getServiceRadiusKm()
        );
        service.setAdvancePercentage(
                request.getAdvancePercentage()
        );

        if (request.getFeatured() != null) {
            service.setFeatured(request.getFeatured());
        }

        if (request.getActive() != null) {
            service.setActive(request.getActive());
        }

        service.getApplicableEventTypes().clear();

        if (request.getApplicableEventTypes() != null) {
            service.getApplicableEventTypes()
                    .addAll(request.getApplicableEventTypes());
        }

        return serviceMapper.toResponse(
                serviceRepository.save(service)
        );
    }

    @Override
    public void delete(
            String userEmail,
            Long serviceId
    ) {
        VendorProfile vendor = findVendor(userEmail);

        ServiceOffering service = serviceRepository
                .findByIdAndVendorIdAndActiveTrue(
                        serviceId,
                        vendor.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service not found or access denied"
                        )
                );

        service.setActive(false);
        serviceRepository.save(service);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> searchPublic(
            String city,
            ServiceType serviceType,
            Long categoryId,
            BigDecimal maxPrice,
            Boolean featured
    ) {
        List<ServiceOffering> services;

        if (city != null && !city.isBlank()) {
            services =
                    serviceRepository
                            .findByCityIgnoreCaseAndActiveTrueOrderByRatingDesc(
                                    city.trim()
                            );

        } else if (serviceType != null) {
            services =
                    serviceRepository
                            .findByServiceTypeAndActiveTrueOrderByRatingDesc(
                                    serviceType
                            );

        } else if (categoryId != null) {
            services =
                    serviceRepository
                            .findByCategoryIdAndActiveTrueOrderByCreatedAtDesc(
                                    categoryId
                            );

        } else if (maxPrice != null) {
            services =
                    serviceRepository
                            .findByBasePriceLessThanEqualAndActiveTrueOrderByBasePriceAsc(
                                    maxPrice
                            );

        } else if (Boolean.TRUE.equals(featured)) {
            services =
                    serviceRepository
                            .findByFeaturedTrueAndActiveTrueOrderByRatingDesc();

        } else {
            services =
                    serviceRepository
                            .findByActiveTrueOrderByCreatedAtDesc();
        }

        return services.stream()
                .map(serviceMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse getPublicById(Long serviceId) {
        ServiceOffering service = serviceRepository
                .findByIdAndActiveTrue(serviceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service not found"
                        )
                );

        return serviceMapper.toResponse(service);
    }

    private VendorProfile findApprovedVendor(String email) {
        VendorProfile vendor = findVendor(email);

        if (vendor.getStatus() != VendorStatus.APPROVED) {
            throw new ConflictException(
                    "Vendor must be approved before creating or updating services"
            );
        }

        return vendor;
    }

    private VendorProfile findVendor(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return vendorRepository
                .findByUserIdAndActiveTrue(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vendor profile not found"
                        )
                );
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        )
                );
    }

    private String generateUniqueSlug(String name) {
        String slug = generateSlug(name);

        if (!serviceRepository.existsBySlug(slug)) {
            return slug;
        }

        return slug + "-" + System.currentTimeMillis();
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }

    private String normalize(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }

    private String valueOrDefault(
            String value,
            String defaultValue
    ) {
        return value == null || value.isBlank()
                ? defaultValue
                : value.trim();
    }
}