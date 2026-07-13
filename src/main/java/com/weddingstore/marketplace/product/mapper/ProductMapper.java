package com.weddingstore.marketplace.product.mapper;

import com.weddingstore.marketplace.product.dto.ProductImageResponse;
import com.weddingstore.marketplace.product.dto.ProductResponse;
import com.weddingstore.marketplace.product.entity.Product;
import com.weddingstore.marketplace.product.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductResponse toResponse(Product product);

    ProductImageResponse toImageResponse(ProductImage image);
}