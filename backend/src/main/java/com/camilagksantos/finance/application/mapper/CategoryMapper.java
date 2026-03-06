package com.camilagksantos.finance.application.mapper;

import com.camilagksantos.finance.application.dto.request.CategoryRequest;
import com.camilagksantos.finance.application.dto.response.CategoryResponse;
import com.camilagksantos.finance.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toDomain(CategoryRequest request);

    CategoryResponse toResponse(Category category);
}
