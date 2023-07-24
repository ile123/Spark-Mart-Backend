package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.BrandDTO;
import com.ilario.sparkmart.dto.DisplayBrandDTO;
import com.ilario.sparkmart.services.IBrandService;
import com.ilario.sparkmart.utility.FileUploadUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        var allBrands = brandService.getAll(page, pageSize, sortBy, sortDir, keyword);
        return new ResponseEntity<>(allBrands, HttpStatus.OK);
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<BrandDTO> GetBrand(@PathVariable("brandId") UUID brandId) {
        var brand = brandService.getById(brandId);
        if(brand == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(brand, HttpStatus.OK);
    }

    @GetMapping("/get-all-brand-displays")
    public ResponseEntity<Page<DisplayBrandDTO>> GetAllDisplayBrands(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int pageSize,
                                                                         @RequestParam(defaultValue = "name") String sortBy,
                                                                         @RequestParam(defaultValue = "asc") String sortDir,
                                                                         @RequestParam(defaultValue = "") String keyword) {
        var allDisplays = brandService.getAllDisplayBrands(page, pageSize, sortBy, sortDir, keyword);
        return new ResponseEntity<>(allDisplays, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> SaveBrand(@RequestParam("image") MultipartFile image, @RequestParam("name") String name) throws IOException {
        brandService.saveToDB(image, name);
        return ResponseEntity.ok("Brand saved successfully");
    }

    @PutMapping("/{brandId}")
    public ResponseEntity<String> UpdateBrand(@PathVariable("brandId") UUID brandId, @RequestParam("image") MultipartFile image, @RequestParam("name") String name) throws IOException {
        var brand = brandService.getById(brandId);
        if(brand == null) {
            return new ResponseEntity<>("ERROR: Brand not found!", HttpStatus.NOT_FOUND);
        }
        try {
            Files.delete(Path.of("src/main/resources/images/brand-photos" + brand.imageName()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String newFileName = FileUploadUtil.removeSpecialCharacters(Objects.requireNonNull(image.getOriginalFilename()));
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        FileUploadUtil.saveFile("brand-photos", fileName, image);
        brandService.update(brandId, new BrandDTO(brandId, name, newFileName));
        return new ResponseEntity<>("Brand successfully updated!", HttpStatus.OK);
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<String> DeleteBrand(@PathVariable("brandId") UUID brandId) {
        var brand = brandService.getBrandFromDB(brandId);
        if(brand == null) {
            return new ResponseEntity<>("ERROR: Brand not found!", HttpStatus.NOT_FOUND);
        }
        brandService.delete(brandId);
        return new ResponseEntity<>("Brand deleted successfully!", HttpStatus.OK);
    }
 }
