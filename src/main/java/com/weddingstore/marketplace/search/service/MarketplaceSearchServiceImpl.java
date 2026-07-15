package com.weddingstore.marketplace.search.service;

import com.weddingstore.common.exception.ConflictException;
import com.weddingstore.common.search.dto.PageResponse;
import com.weddingstore.common.search.util.PageResponseMapper;
import com.weddingstore.common.search.util.PageableUtil;
import com.weddingstore.marketplace.product.dto.ProductResponse;
import com.weddingstore.marketplace.product.entity.Product;
import com.weddingstore.marketplace.product.mapper.ProductMapper;
import com.weddingstore.marketplace.product.repository.ProductRepository;
import com.weddingstore.marketplace.search.dto.ProductSearchRequest;
import com.weddingstore.marketplace.search.dto.ServiceSearchRequest;
import com.weddingstore.marketplace.search.dto.VendorSearchRequest;
import com.weddingstore.marketplace.search.specification.ProductSpecification;
import com.weddingstore.marketplace.search.specification.ServiceOfferingSpecification;
import com.weddingstore.marketplace.search.specification.VendorSpecification;
import com.weddingstore.marketplace.service.dto.ServiceResponse;
import com.weddingstore.marketplace.service.entity.ServiceOffering;
import com.weddingstore.marketplace.service.mapper.ServiceOfferingMapper;
import com.weddingstore.marketplace.service.repository.ServiceOfferingRepository;
import com.weddingstore.marketplace.vendor.dto.VendorResponse;
import com.weddingstore.marketplace.vendor.entity.VendorProfile;
import com.weddingstore.marketplace.vendor.mapper.VendorMapper;
import com.weddingstore.marketplace.vendor.repository.VendorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarketplaceSearchServiceImpl
        implements MarketplaceSearchService {

    private static final Set<String> PRODUCT_SORT_FIELDS =
            Set.of(
                    "createdAt",
                    "name",
                    "price",
                    "discountPrice",
                    "quantity",
                    "featured"
            );

    private static final Set<String> SERVICE_SORT_FIELDS =
            Set.of(
                    "createdAt",
                    "name",
                    "basePrice",
                    "rating",
                    "featured",
                    "city"
            );

    private static final Set<String> VENDOR_SORT_FIELDS =
            Set.of(
                    "createdAt",
                    "businessName",
                    "rating",
                    "city",
                    "verified"
            );

    private final ProductRepository productRepository;
    private final ServiceOfferingRepository serviceRepository;
    private final VendorProfileRepository vendorRepository;

    private final ProductMapper productMapper;
    private final ServiceOfferingMapper serviceMapper;
    private final VendorMapper vendorMapper;

    @Override
    public PageResponse<ProductResponse> searchProducts(
            ProductSearchRequest request
    ) {
        validatePriceRange(
                request.getMinPrice(),
                request.getMaxPrice()
        );

        Pageable pageable = PageableUtil.create(
                request,
                PRODUCT_SORT_FIELDS
        );

        Specification<Product> specification =
                Specification
                        .where(ProductSpecification.active())
                        .and(ProductSpecification.keyword(
                                request.getKeyword()
                        ))
                        .and(ProductSpecification.category(
                                request.getCategoryId()
                        ))
                        .and(ProductSpecification.minEffectivePrice(
                                request.getMinPrice()
                        ))
                        .and(ProductSpecification.maxEffectivePrice(
                                request.getMaxPrice()
                        ))
                        .and(ProductSpecification.featured(
                                request.getFeatured()
                        ))
                        .and(ProductSpecification.inStock(
                                request.getInStock()
                        ));

        Page<Product> page = productRepository.findAll(
                specification,
                pageable
        );

        return PageResponseMapper.map(
                page,
                productMapper::toResponse,
                request.getSortBy(),
                request.getDirection()
        );
    }

    @Override
    public PageResponse<ServiceResponse> searchServices(
            ServiceSearchRequest request
    ) {
        validatePriceRange(
                request.getMinPrice(),
                request.getMaxPrice()
        );

        Pageable pageable = PageableUtil.create(
                request,
                SERVICE_SORT_FIELDS
        );

        Specification<ServiceOffering> specification =
                Specification
                        .where(
                                ServiceOfferingSpecification.active()
                        )
                        .and(
                                ServiceOfferingSpecification
                                        .approvedVendor()
                        )
                        .and(
                                ServiceOfferingSpecification.keyword(
                                        request.getKeyword()
                                )
                        )
                        .and(
                                ServiceOfferingSpecification.city(
                                        request.getCity()
                                )
                        )
                        .and(
                                ServiceOfferingSpecification.category(
                                        request.getCategoryId()
                                )
                        )
                        .and(
                                ServiceOfferingSpecification.vendor(
                                        request.getVendorId()
                                )
                        )
                        .and(
                                ServiceOfferingSpecification.serviceType(
                                        request.getServiceType()
                                )
                        )
                        .and(
                                ServiceOfferingSpecification.locationType(
                                        request.getLocationType()
                                )
                        )
                        .and(
                                ServiceOfferingSpecification.eventType(
                                        request.getEventType()
                                )
                        )
                        .and(
                                ServiceOfferingSpecification.minPrice(
                                        request.getMinPrice()
                                )
                        )
                        .and(
                                ServiceOfferingSpecification.maxPrice(
                                        request.getMaxPrice()
                                )
                        )
                        .and(
                                ServiceOfferingSpecification.minRating(
                                        request.getMinRating()
                                )
                        )
                        .and(
                                ServiceOfferingSpecification.featured(
                                        request.getFeatured()
                                )
                        );

        Page<ServiceOffering> page =
                serviceRepository.findAll(
                        specification,
                        pageable
                );

        return PageResponseMapper.map(
                page,
                serviceMapper::toResponse,
                request.getSortBy(),
                request.getDirection()
        );
    }

    @Override
    public PageResponse<VendorResponse> searchVendors(
            VendorSearchRequest request
    ) {
        Pageable pageable = PageableUtil.create(
                request,
                VENDOR_SORT_FIELDS
        );

        Specification<VendorProfile> specification =
                Specification
                        .where(VendorSpecification.active())
                        .and(VendorSpecification.approved())
                        .and(VendorSpecification.keyword(
                                request.getKeyword()
                        ))
                        .and(VendorSpecification.city(
                                request.getCity()
                        ))
                        .and(VendorSpecification.state(
                                request.getState()
                        ))
                        .and(VendorSpecification.vendorType(
                                request.getVendorType()
                        ))
                        .and(VendorSpecification.verified(
                                request.getVerified()
                        ))
                        .and(VendorSpecification.minRating(
                                request.getMinRating()
                        ));

        Page<VendorProfile> page =
                vendorRepository.findAll(
                        specification,
                        pageable
                );

        return PageResponseMapper.map(
                page,
                vendorMapper::toResponse,
                request.getSortBy(),
                request.getDirection()
        );
    }

    private void validatePriceRange(
            java.math.BigDecimal minPrice,
            java.math.BigDecimal maxPrice
    ) {
        if (minPrice != null
                && maxPrice != null
                && minPrice.compareTo(maxPrice) > 0) {

            throw new ConflictException(
                    "Minimum price cannot be greater than maximum price"
            );
        }
    }
}