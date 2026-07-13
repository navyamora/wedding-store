package com.weddingstore.marketplace.vendor.service;

import com.weddingstore.common.exception.ConflictException;
import com.weddingstore.common.exception.ResourceNotFoundException;
import com.weddingstore.marketplace.vendor.dto.CreateVendorRequest;
import com.weddingstore.marketplace.vendor.dto.UpdateVendorRequest;
import com.weddingstore.marketplace.vendor.dto.VendorResponse;
import com.weddingstore.marketplace.vendor.entity.VendorProfile;
import com.weddingstore.marketplace.vendor.entity.VendorStatus;
import com.weddingstore.marketplace.vendor.mapper.VendorMapper;
import com.weddingstore.marketplace.vendor.repository.VendorProfileRepository;
import com.weddingstore.user.entity.User;
import com.weddingstore.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VendorServiceImpl implements VendorService {

    private final VendorProfileRepository vendorRepository;
    private final UserRepository userRepository;
    private final VendorMapper vendorMapper;

    public VendorServiceImpl(
            VendorProfileRepository vendorRepository,
            UserRepository userRepository,
            VendorMapper vendorMapper
    ) {
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
        this.vendorMapper = vendorMapper;
    }

    @Override
    public VendorResponse createMyProfile(
            String userEmail,
            CreateVendorRequest request
    ) {
        User user = findUserByEmail(userEmail);

        if (vendorRepository.existsByUserId(user.getId())) {
            throw new ConflictException(
                    "A vendor profile already exists for this user"
            );
        }

        VendorProfile vendor = VendorProfile.builder()
                .user(user)
                .businessName(request.getBusinessName().trim())
                .ownerName(request.getOwnerName().trim())
                .email(request.getEmail().trim().toLowerCase())
                .mobile(request.getMobile().trim())
                .vendorType(request.getVendorType())
                .gstNumber(normalizeNullable(request.getGstNumber()))
                .panNumber(normalizeNullable(request.getPanNumber()))
                .city(request.getCity().trim())
                .state(request.getState().trim())
                .country(request.getCountry().trim())
                .address(request.getAddress().trim())
                .description(normalizeNullable(request.getDescription()))
                .logoUrl(normalizeNullable(request.getLogoUrl()))
                .status(VendorStatus.PENDING)
                .verified(false)
                .active(true)
                .build();

        return vendorMapper.toResponse(
                vendorRepository.save(vendor)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public VendorResponse getMyProfile(String userEmail) {
        User user = findUserByEmail(userEmail);

        VendorProfile vendor = vendorRepository
                .findByUserIdAndActiveTrue(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vendor profile not found"
                        )
                );

        return vendorMapper.toResponse(vendor);
    }

    @Override
    public VendorResponse updateMyProfile(
            String userEmail,
            UpdateVendorRequest request
    ) {
        User user = findUserByEmail(userEmail);

        VendorProfile vendor = vendorRepository
                .findByUserIdAndActiveTrue(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vendor profile not found"
                        )
                );

        vendor.setBusinessName(request.getBusinessName().trim());
        vendor.setOwnerName(request.getOwnerName().trim());
        vendor.setEmail(request.getEmail().trim().toLowerCase());
        vendor.setMobile(request.getMobile().trim());
        vendor.setVendorType(request.getVendorType());
        vendor.setGstNumber(normalizeNullable(request.getGstNumber()));
        vendor.setPanNumber(normalizeNullable(request.getPanNumber()));
        vendor.setCity(request.getCity().trim());
        vendor.setState(request.getState().trim());
        vendor.setCountry(request.getCountry().trim());
        vendor.setAddress(request.getAddress().trim());
        vendor.setDescription(normalizeNullable(request.getDescription()));
        vendor.setLogoUrl(normalizeNullable(request.getLogoUrl()));

        if (vendor.getStatus() == VendorStatus.REJECTED) {
            vendor.setStatus(VendorStatus.PENDING);
            vendor.setVerified(false);
        }

        return vendorMapper.toResponse(
                vendorRepository.save(vendor)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorResponse> getApprovedVendors(String city) {
        List<VendorProfile> vendors;

        if (city == null || city.isBlank()) {
            vendors = vendorRepository
                    .findByStatusAndActiveTrueOrderByCreatedAtDesc(
                            VendorStatus.APPROVED
                    );
        } else {
            vendors = vendorRepository
                    .findByCityIgnoreCaseAndStatusAndActiveTrueOrderByRatingDesc(
                            city.trim(),
                            VendorStatus.APPROVED
                    );
        }

        return vendors.stream()
                .map(vendorMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorResponse> getAllForAdmin(VendorStatus status) {
        List<VendorProfile> vendors;

        if (status == null) {
            vendors = vendorRepository
                    .findByActiveTrueOrderByCreatedAtDesc();
        } else {
            vendors = vendorRepository
                    .findByStatusAndActiveTrueOrderByCreatedAtDesc(status);
        }

        return vendors.stream()
                .map(vendorMapper::toResponse)
                .toList();
    }

    @Override
    public VendorResponse updateStatus(
            Long vendorId,
            VendorStatus status
    ) {
        VendorProfile vendor = vendorRepository
                .findByIdAndActiveTrue(vendorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vendor profile not found"
                        )
                );

        if (status == VendorStatus.PENDING) {
            throw new ConflictException(
                    "Admin cannot manually move a vendor back to pending"
            );
        }

        vendor.setStatus(status);
        vendor.setVerified(status == VendorStatus.APPROVED);

        return vendorMapper.toResponse(
                vendorRepository.save(vendor)
        );
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    private String normalizeNullable(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }
}