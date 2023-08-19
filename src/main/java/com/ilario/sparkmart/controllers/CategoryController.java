package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.CategoryDTO;
import com.ilario.sparkmart.dto.CategoryRequestDTO;
import com.ilario.sparkmart.dto.DisplayCategoryDTO;
import com.ilario.sparkmart.exceptions.categories.CategoriesNotFoundException;
import com.ilario.sparkmart.exceptions.categories.CategoryNotFoundException;
import com.ilario.sparkmart.services.ICategoryService;
import com.ilario.sparkmart.utility.FileUploadUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<Page<CategoryDTO>> GetAllCategories(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int pageSize,
                                                          @RequestParam(defaultValue = "name") String sortBy,
                                                          @RequestParam(defaultValue = "asc") String sortDir,
                                                          @RequestParam(defaultValue = "") String keyword) {
        try {
            var allCategories = categoryService.getAll(page, pageSize, sortBy, sortDir, keyword);
            return ResponseEntity.ok(allCategories);
        } catch (CategoriesNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get-all-category-displays")
    public ResponseEntity<Page<DisplayCategoryDTO>> GetAllDisplayCategories(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int pageSize,
                                                                            @RequestParam(defaultValue = "name") String sortBy,
                                                                            @RequestParam(defaultValue = "asc") String sortDir,
                                                                            @RequestParam(defaultValue = "") String keyword) {
        try {
            var allCategories = categoryService.getAllCategoryDisplays(page, pageSize, sortBy, sortDir, keyword);
            return ResponseEntity.ok(allCategories);
        } catch (CategoriesNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> GetCategory(@PathVariable("categoryId") UUID brandId) {
        try {
            var category = categoryService.getById(brandId);
            return ResponseEntity.ok(category);
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<String> SaveCategory(@ModelAttribute CategoryRequestDTO categoryRequestDTO) {
        try {
            categoryService.saveToDB(categoryRequestDTO.image(), categoryRequestDTO.name(), categoryRequestDTO.description());
            return ResponseEntity.status(HttpStatus.CREATED).body("Category saved successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR: Category not saved.");
        }
    }
    @PutMapping("/{categoryId}")
    public ResponseEntity<String> UpdateCategory(@PathVariable("categoryId") UUID categoryId, @ModelAttribute CategoryRequestDTO categoryRequestDTO) {
        try {
            if(categoryRequestDTO.image() != null) {
                String newFileName = FileUploadUtil.removeSpecialCharacters(Objects.requireNonNull(categoryRequestDTO.image().getOriginalFilename()));
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(categoryRequestDTO.image().getOriginalFilename()));
                FileUploadUtil.saveFile("category-photos", fileName, categoryRequestDTO.image());
                categoryService.update(categoryRequestDTO.id(), new CategoryDTO(categoryRequestDTO.id(), categoryRequestDTO.name(), categoryRequestDTO.description(), newFileName));
            } else {
                categoryService.update(categoryRequestDTO.id(), new CategoryDTO(categoryId, categoryRequestDTO.name(), categoryRequestDTO.description(), ""));
            }
            return ResponseEntity.ok("Category successfully updated!");
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> DeleteCategory(@PathVariable("categoryId") UUID categoryId) {
        try {
            categoryService.delete(categoryId);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
