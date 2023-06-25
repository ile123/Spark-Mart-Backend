package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.BrandDTO;
import com.ilario.sparkmart.dto.CategoryDTO;
import com.ilario.sparkmart.models.Category;
import com.ilario.sparkmart.services.ICategoryService;
import com.ilario.sparkmart.utility.FileUploadUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        var allCategories = categoryService.getAll(page, pageSize, sortBy, sortDir, keyword);
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> GetCategory(@PathVariable("categoryId") UUID brandId) {
        var category = categoryService.getById(brandId);
        if(category == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> SaveCategory(@RequestParam("image") MultipartFile image, @RequestParam("name") String name, @RequestParam("description") String description) throws IOException {
        categoryService.saveCategoryToTheDB(image, name, description);
        return ResponseEntity.ok("Category saved successfully");
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<String> UpdateCategory(@PathVariable("categoryId") UUID categoryId, @RequestParam("image") MultipartFile image, @RequestParam("name") String name, @RequestParam("description") String description) throws IOException {
        var category = categoryService.getById(categoryId);
        if(category == null) {
            return new ResponseEntity<>("ERROR: Category not found!", HttpStatus.NOT_FOUND);
        }
        String newFileName = FileUploadUtil.removeSpecialCharacters(Objects.requireNonNull(image.getOriginalFilename()));
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        String uploadDir = "src/main/resources/images/category-photos";
        FileUploadUtil.saveFile(uploadDir, fileName, image);
        categoryService.update(categoryId, new CategoryDTO(categoryId, name, description, newFileName));
        return new ResponseEntity<>("Category successfully updated!", HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> DeleteCategory(@PathVariable("categoryId") UUID categoryId) {
        var category = categoryService.getCategoryFromDB(categoryId);
        if(category == null) {
            return new ResponseEntity<>("ERROR: Category not found!", HttpStatus.NOT_FOUND);
        }
        categoryService.delete(categoryId);
        return new ResponseEntity<>("Category deleted successfully!", HttpStatus.OK);
    }
}
