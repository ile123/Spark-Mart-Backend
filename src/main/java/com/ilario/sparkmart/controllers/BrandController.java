package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.BrandDTO;
import com.ilario.sparkmart.dto.BrandRequestDTO;
import com.ilario.sparkmart.dto.DisplayBrandDTO;
import com.ilario.sparkmart.exceptions.brands.BrandNotFoundException;
import com.ilario.sparkmart.exceptions.brands.BrandsNotFoundException;
import com.ilario.sparkmart.services.IBrandService;
import com.ilario.sparkmart.utility.FileUploadUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Objects;
import java.util.UUID;
@RestController
@RequestMapping("/brands")
public class BrandController {

    private final IBrandService brandService;

    public BrandController(IBrandService brandService) { this.brandService = brandService; }

    @GetMapping("")
    public ResponseEntity<Page<BrandDTO>> GetAllBrands(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int pageSize,
                                                             @RequestParam(defaultValue = "name") String sortBy,
                                                             @RequestParam(defaultValue = "asc") String sortDir,
                                                             @RequestParam(defaultValue = "") String keyword) {
        try {
            var allBrands = brandService.getAll(page, pageSize, sortBy, sortDir, keyword);
            return ResponseEntity.ok(allBrands);
        } catch (BrandsNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get-all-brand-displays")
    public ResponseEntity<Page<DisplayBrandDTO>> GetAllDisplayBrands(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int pageSize,
                                                                     @RequestParam(defaultValue = "name") String sortBy,
                                                                     @RequestParam(defaultValue = "asc") String sortDir,
                                                                     @RequestParam(defaultValue = "") String keyword) {
        try {
            var allBrands = brandService.getAllDisplayBrands(page ,pageSize, sortBy, sortDir, keyword);
            return ResponseEntity.ok(allBrands);
        } catch (BrandsNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<BrandDTO> GetBrand(@PathVariable("brandId") UUID brandId) {
        try {
            var brand = brandService.getById(brandId);
            return ResponseEntity.ok(brand);
        } catch (BrandNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<String> SaveBrand(@ModelAttribute BrandRequestDTO brandRequestDTO) {
        try {
            brandService.saveToDB(brandRequestDTO.image(), brandRequestDTO.name());
            return ResponseEntity.status(HttpStatus.CREATED).body("Brand saved successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR: Brand not saved.");
        }
    }

    @PutMapping("/{brandId}")
    public ResponseEntity<String> UpdateBrand(@PathVariable UUID brandId, @ModelAttribute BrandRequestDTO brandRequestDTO) {
        try {
            if(brandRequestDTO.image() != null) {
                String newFileName = FileUploadUtil.removeSpecialCharacters(Objects.requireNonNull(brandRequestDTO.image().getOriginalFilename()));
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(brandRequestDTO.image().getOriginalFilename()));
                FileUploadUtil.saveFile("brand-photos", fileName, brandRequestDTO.image());
                brandService.update(brandId, new BrandDTO(brandId, brandRequestDTO.name(), newFileName));
            } else {
                brandService.update(brandId, new BrandDTO(brandId, brandRequestDTO.name(), ""));
            }
            return ResponseEntity.ok("Brand successfully updated!");
        } catch (BrandNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<String> DeleteBrand(@PathVariable("brandId") UUID brandId) {
        try {
            brandService.delete(brandId);
            return ResponseEntity.ok("Brand deleted successfully.");
        } catch (BrandNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
 }
