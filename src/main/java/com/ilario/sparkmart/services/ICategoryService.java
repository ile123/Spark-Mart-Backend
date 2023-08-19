package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.CategoryDTO;
import com.ilario.sparkmart.dto.DisplayCategoryDTO;
import com.ilario.sparkmart.exceptions.categories.CategoriesNotFoundException;
import com.ilario.sparkmart.exceptions.categories.CategoryNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ICategoryService {
    CategoryDTO getById(UUID id) throws CategoryNotFoundException;
    void saveToDB(MultipartFile image, String name, String description) throws IOException;
    Page<CategoryDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword) throws CategoriesNotFoundException;
    void update(UUID id, CategoryDTO categoryDTO) throws CategoryNotFoundException;
    void delete(UUID id) throws CategoryNotFoundException;
    Page<DisplayCategoryDTO> getAllCategoryDisplays(int page, int pageSize, String sortBy, String sortDir, String keyword) throws CategoriesNotFoundException;
}
