package com.weddingstore.marketplace.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.weddingstore.marketplace.category.dto.CategoryResponse;
import com.weddingstore.marketplace.category.entity.Category;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

}