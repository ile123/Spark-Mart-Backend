package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.BrandDTO;
import com.ilario.sparkmart.dto.DisplayBrandDTO;
import com.ilario.sparkmart.exceptions.brands.BrandNotFoundException;
import com.ilario.sparkmart.exceptions.brands.BrandsNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface IBrandService {
    BrandDTO getById(UUID id) throws BrandNotFoundException;
    void saveToDB(MultipartFile image, String name) throws IOException;
    Page<BrandDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword) throws BrandsNotFoundException;
    void update(UUID id, BrandDTO brandDTO) throws BrandNotFoundException;
    void delete(UUID id) throws BrandNotFoundException;
    Page<DisplayBrandDTO> getAllDisplayBrands(int page, int pageSize, String sortBy, String sortDir, String keyword) throws BrandsNotFoundException;
}
