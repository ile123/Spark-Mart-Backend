package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.CategoryDTO;
import com.ilario.sparkmart.dto.DisplayCategoryDTO;
import com.ilario.sparkmart.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ICategoryService extends IBaseService<CategoryDTO, UUID> {
    void saveCategoryToTheDB(MultipartFile image, String name, String description) throws IOException;

    Category getCategoryFromDB(UUID id);

    Page<DisplayCategoryDTO> getAllCategoryDisplays(int page, int pageSize, String sortBy, String sortDir, String keyword);
}
