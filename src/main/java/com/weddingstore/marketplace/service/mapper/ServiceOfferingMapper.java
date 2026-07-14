package com.weddingstore.marketplace.service.mapper;

import com.weddingstore.marketplace.service.dto.ServiceImageResponse;
import com.weddingstore.marketplace.service.dto.ServicePackageResponse;
import com.weddingstore.marketplace.service.dto.ServiceResponse;
import com.weddingstore.marketplace.service.entity.ServiceImage;
import com.weddingstore.marketplace.service.entity.ServiceOffering;
import com.weddingstore.marketplace.service.entity.ServicePackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceOfferingMapper {

    @Mapping(source = "vendor.id", target = "vendorId")
    @Mapping(source = "vendor.businessName", target = "vendorBusinessName")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ServiceResponse toResponse(ServiceOffering service);

    ServiceImageResponse toImageResponse(ServiceImage image);

    ServicePackageResponse toPackageResponse(ServicePackage servicePackage);
}