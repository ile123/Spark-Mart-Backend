package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.CategoryDTO;
import com.ilario.sparkmart.exceptions.brands.BrandNotFoundException;
import com.ilario.sparkmart.exceptions.categories.CategoryNotFoundException;
import com.ilario.sparkmart.mappers.CategoryMapper;
import com.ilario.sparkmart.models.Category;
import com.ilario.sparkmart.repositories.ICategoryRepository;
import com.ilario.sparkmart.services.ICategoryService;
import com.ilario.sparkmart.utility.FileUploadUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(ICategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDTO getById(UUID uuid) {
        try {
            var category = categoryRepository.findById(uuid);
            if(category.isEmpty()) {
                throw new CategoryNotFoundException("ERROR: Category not found by given ID!");
            }
            return categoryMapper.toCategoryDTO(category.get());
        } catch (CategoryNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public void saveToDB(CategoryDTO entity) {}

    @Override
    public Page<CategoryDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Category> pageResult;
        if(keyword.isEmpty()) {
            pageResult = categoryRepository.findAll(pageable);
        } else {
            pageResult = categoryRepository.findAllByKeyword(keyword, pageable);
        }
        var categoriesDTO = pageResult
                .getContent()
                .stream()
                .filter(Category::isEnabled)
                .map(categoryMapper::toCategoryDTO)
                .toList();
        return new PageImpl<>(categoriesDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public void update(UUID uuid, CategoryDTO entity) {
        if(!categoryRepository.existsById(uuid)) return;
        var category = categoryRepository.getReferenceById(uuid);
        category.setName(entity.name());
        category.setDescription(entity.description());
        category.setPicture(entity.imageName());
        categoryRepository.save(category);
    }

    @Override
    public void delete(UUID uuid) {
        var category = categoryRepository.findById(uuid);
        if(category.isEmpty()) {
            return;
        }
        if(!category.get().getProducts().isEmpty()) {
            for(var product : category.get().getProducts()) {
                product.setIsDisabled(true);
            }
            category.get().setDisabled(true);
            categoryRepository.save(category.get());
            return;
        }
        categoryRepository.delete(category.get());
    }

    @Override
    public void saveCategoryToTheDB(MultipartFile image, String name, String description) throws IOException {
        var category = new Category();
        category.setName(name);
        category.setDescription(description);
        String newFileName = FileUploadUtil.removeSpecialCharacters(Objects.requireNonNull(image.getOriginalFilename()));
        category.setPicture(newFileName);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        String uploadDir = "src/main/resources/images/category-photos";
        FileUploadUtil.saveFile(uploadDir, fileName, image);
        categoryRepository.save(category);
    }

    @Override
    public Category getCategoryFromDB(UUID id) {
        return categoryRepository.getReferenceById(id);
    }
}
