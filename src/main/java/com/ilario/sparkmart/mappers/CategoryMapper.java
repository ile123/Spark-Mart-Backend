package com.ilario.sparkmart.mappers;

import com.ilario.sparkmart.dto.BrandDTO;
import com.ilario.sparkmart.dto.CategoryDTO;
import com.ilario.sparkmart.dto.DisplayCategoryDTO;
import com.ilario.sparkmart.dto.DisplayProductDTO;
import com.ilario.sparkmart.models.Brand;
import com.ilario.sparkmart.models.Category;
import com.ilario.sparkmart.models.Product;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryDTO toCategoryDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription(), category.getPicture());
    }

    public DisplayCategoryDTO toDisplayCategoryDTO(Category category) {
        return new DisplayCategoryDTO(category.getId(), category.getName(), category.getPicture());
    }
}
