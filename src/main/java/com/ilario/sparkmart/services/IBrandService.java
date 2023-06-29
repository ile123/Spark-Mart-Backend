package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.BrandDTO;
import com.ilario.sparkmart.dto.DisplayBrandDTO;
import com.ilario.sparkmart.models.Brand;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface IBrandService extends IBaseService<BrandDTO, UUID> {
    void saveBrandToTheDB(MultipartFile image, String name) throws IOException;

    Brand getBrandFromDB(UUID id);

    Page<DisplayBrandDTO> getAllDisplayBrands(int page, int pageSize, String sortBy, String sortDir, String keyword);
}
