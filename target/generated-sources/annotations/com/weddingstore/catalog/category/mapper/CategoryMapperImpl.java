package com.weddingstore.catalog.category.mapper;

import com.weddingstore.catalog.category.dto.CategoryResponse;
import com.weddingstore.catalog.category.entity.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-07T17:29:31-0400",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryResponse toResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder categoryResponse = CategoryResponse.builder();

        categoryResponse.id( category.getId() );
        categoryResponse.name( category.getName() );
        categoryResponse.slug( category.getSlug() );
        categoryResponse.description( category.getDescription() );
        categoryResponse.imageUrl( category.getImageUrl() );
        categoryResponse.displayOrder( category.getDisplayOrder() );
        categoryResponse.active( category.getActive() );

        return categoryResponse.build();
    }
}
