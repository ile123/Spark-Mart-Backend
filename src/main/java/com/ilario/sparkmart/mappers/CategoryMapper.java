package com.ilario.sparkmart.mappers;

import com.ilario.sparkmart.dto.BrandDTO;
import com.ilario.sparkmart.dto.CategoryDTO;
import com.ilario.sparkmart.models.Brand;
import com.ilario.sparkmart.models.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryDTO toCategoryDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription(), category.getPicture());
    }
}
