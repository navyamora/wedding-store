package com.weddingstore.catalog.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.weddingstore.catalog.category.dto.CategoryResponse;
import com.weddingstore.catalog.category.entity.Category;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

}