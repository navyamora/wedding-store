package com.weddingstore.marketplace.vendor.mapper;

import com.weddingstore.marketplace.vendor.dto.VendorResponse;
import com.weddingstore.marketplace.vendor.entity.VendorProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VendorMapper {

    @Mapping(source = "user.id", target = "userId")
    VendorResponse toResponse(VendorProfile vendor);
}