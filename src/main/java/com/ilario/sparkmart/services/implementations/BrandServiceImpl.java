package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.BrandDTO;
import com.ilario.sparkmart.dto.DisplayBrandDTO;
import com.ilario.sparkmart.exceptions.brands.BrandNotFoundException;
import com.ilario.sparkmart.mappers.BrandMapper;
import com.ilario.sparkmart.models.Brand;
import com.ilario.sparkmart.repositories.IBrandRepository;
import com.ilario.sparkmart.services.IBrandService;
import com.ilario.sparkmart.utility.FileUploadUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class BrandServiceImpl implements IBrandService {

    private final IBrandRepository brandRepository;

    private final BrandMapper brandMapper;

    public BrandServiceImpl(IBrandRepository brandRepository, BrandMapper brandMapper) { this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    public BrandDTO getById(UUID uuid) {
        try {
            var brand = brandRepository.findById(uuid);
            if(brand.isEmpty()) {
                throw new BrandNotFoundException("ERROR: Brand not found by given ID!");
            }
            return brandMapper.toBrandDTO(brand.get());
        } catch (BrandNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public Page<BrandDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Brand> pageResult;
        if(keyword.isEmpty()) {
            pageResult = brandRepository.findAll(pageable);
        } else {
            pageResult = brandRepository.findAllByKeyword(keyword, pageable);
        }
        var brandsDTO = pageResult
                .getContent()
                .stream()
                .filter(Brand::isEnabled)
                .map(brandMapper::toBrandDTO)
                .toList();
        return new PageImpl<>(brandsDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public void update(UUID uuid, BrandDTO entity) {
        if(!brandRepository.existsById(uuid)) return;
        var brand = brandRepository.getReferenceById(uuid);
        brand.setName(entity.name());
        brand.setPicture(entity.imageName());
        brandRepository.save(brand);
    }

    @Override
    public void delete(UUID uuid) {
        var brand = brandRepository.findById(uuid);
        if(brand.isEmpty()) {
            return;
        }
        if(!brand.get().getProducts().isEmpty()) {
            for(var product : brand.get().getProducts()) {
                product.setIsDisabled(true);
            }
            brand.get().setDisabled(true);
            brandRepository.save(brand.get());
            return;
        }
        brandRepository.delete(brand.get());
    }

    @Override
    public void saveToDB(MultipartFile image, String name) throws IOException {
        var brand = new Brand();
        brand.setName(name);
        String newFileName = FileUploadUtil.removeSpecialCharacters(Objects.requireNonNull(image.getOriginalFilename()));
        brand.setPicture(newFileName);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        FileUploadUtil.saveFile("brand-photos", fileName, image);
        brandRepository.save(brand);
    }

    @Override
    public Brand getBrandFromDB(UUID id) {
        return brandRepository.getReferenceById(id);
    }

    @Override
    public Page<DisplayBrandDTO> getAllDisplayBrands(int page, int pageSize, String sortBy, String sortDir, String keyword) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Brand> pageResult;
        if(keyword.isEmpty()) {
            pageResult = brandRepository.findAll(pageable);
        } else {
            pageResult = brandRepository.findAllByKeyword(keyword, pageable);
        }
        var brandsDTO = pageResult
                .getContent()
                .stream()
                .filter(Brand::isEnabled)
                .map(brandMapper::toDisplayBrandDTO)
                .toList();
        return new PageImpl<>(brandsDTO, pageable, pageResult.getTotalElements());
    }
}
