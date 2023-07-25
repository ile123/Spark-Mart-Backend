package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.CategoryDTO;
import com.ilario.sparkmart.dto.DisplayCategoryDTO;
import com.ilario.sparkmart.exceptions.categories.CategoriesNotFoundException;
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
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(ICategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDTO getById(UUID uuid) throws CategoryNotFoundException {
        var category = categoryRepository.findById(uuid);
        if(category.isEmpty()) {
            throw new CategoryNotFoundException("ERROR: Category by given ID not found.");
        }
        return categoryMapper.toCategoryDTO(category.get());
    }

    @Override
    public Page<CategoryDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword) throws CategoriesNotFoundException {
        var pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var pageResult = keyword.isEmpty() ?
                categoryRepository.findAll(pageable) :
                categoryRepository.findAllByKeyword(keyword.toLowerCase(), pageable);
        if(pageResult.isEmpty()) {
            throw new CategoriesNotFoundException("ERROR: Categories not found.");
        }
        var categoriesDTO = pageResult
                .getContent()
                .stream()
                .filter(Category::isEnabled)
                .map(categoryMapper::toCategoryDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(categoriesDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public Page<DisplayCategoryDTO> getAllCategoryDisplays(int page, int pageSize, String sortBy, String sortDir, String keyword) throws CategoriesNotFoundException {
        var pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var pageResult = keyword.isEmpty() ?
                categoryRepository.findAll(pageable) :
                categoryRepository.findAllByKeyword(keyword.toLowerCase(), pageable);
        if(pageResult.isEmpty()) {
            throw new CategoriesNotFoundException("ERROR: Categories not found.");
        }
        var categoriesDTO = pageResult
                .getContent()
                .stream()
                .filter(Category::isEnabled)
                .map(categoryMapper::toDisplayCategoryDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(categoriesDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public void update(UUID uuid, CategoryDTO entity) throws CategoryNotFoundException {
        var category = categoryRepository
                .findById(uuid)
                .orElseThrow(() -> new CategoryNotFoundException("ERROR: Category by given ID not found."));
        category.setName(entity.name());
        category.setDescription(entity.description());
        category.setPicture(entity.imageName());
        categoryRepository.save(category);
    }

    @Override
    public void delete(UUID uuid) throws CategoryNotFoundException{
        var category = categoryRepository
                .findById(uuid)
                .orElseThrow(() -> new CategoryNotFoundException("Category with the given ID not found."));
        if (!category.getProducts().isEmpty()) {
            category.getProducts().forEach(product -> product.setIsDisabled(true));
            category.setDisabled(true);
            categoryRepository.save(category);
        } else {
            categoryRepository.delete(category);
        }
    }

    @Override
    public void saveToDB(MultipartFile image, String name, String description) throws IOException {
        String newFileName = FileUploadUtil.removeSpecialCharacters(Objects.requireNonNull(image.getOriginalFilename()));
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        FileUploadUtil.saveFile("category-photos", fileName, image);
        var category = Category
                .builder()
                .name(name)
                .description(description)
                .picture(newFileName)
                .build();
        categoryRepository.save(category);
    }
}
