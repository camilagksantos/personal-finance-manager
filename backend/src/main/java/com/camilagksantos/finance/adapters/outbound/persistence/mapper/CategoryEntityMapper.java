package com.camilagksantos.finance.adapters.outbound.persistence.mapper;

import com.camilagksantos.finance.adapters.outbound.persistence.entity.CategoryEntity;
import com.camilagksantos.finance.domain.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryEntityMapper {

    CategoryEntity toEntity(Category category);

    Category toDomain(CategoryEntity entity);
}
