package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.BrandDTO;
import com.ilario.sparkmart.dto.DisplayBrandDTO;
import com.ilario.sparkmart.exceptions.brands.BrandNotFoundException;
import com.ilario.sparkmart.exceptions.brands.BrandsNotFoundException;
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements IBrandService {

    private final IBrandRepository brandRepository;

    private final BrandMapper brandMapper;

    public BrandServiceImpl(IBrandRepository brandRepository, BrandMapper brandMapper) { this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    public BrandDTO getById(UUID uuid) throws BrandNotFoundException {
        var brand = brandRepository.findById(uuid).orElseThrow(() -> new BrandNotFoundException("ERROR: Brand not found."));
        return brandMapper.toBrandDTO(brand);
    }

    @Override
    public Page<BrandDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword) throws BrandsNotFoundException {
        var pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var pageResult = keyword.isEmpty() ?
                brandRepository.findAll(pageable) :
                brandRepository.findAllByKeyword(keyword.toLowerCase(), pageable);
        var brandsDTO = pageResult
                .getContent()
                .stream()
                .filter(Brand::isEnabled)
                .map(brandMapper::toBrandDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(brandsDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public Page<DisplayBrandDTO> getAllDisplayBrands(int page, int pageSize, String sortBy, String sortDir, String keyword) {
        var pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var pageResult = keyword.isEmpty() ?
                brandRepository.findAll(pageable) :
                brandRepository.findAllByKeyword(keyword.toLowerCase(), pageable);
        var brandsDTO = pageResult
                .getContent()
                .stream()
                .filter(Brand::isEnabled)
                .map(brandMapper::toDisplayBrandDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(brandsDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public void update(UUID uuid, BrandDTO entity) throws BrandNotFoundException {
        var brand = brandRepository
                .findById(uuid)
                .orElseThrow(() -> new BrandNotFoundException("Brand with the given ID not found."));
        brand.setName(entity.name());
        if(!entity.imageName().isEmpty()) {
            brand.setPicture(entity.imageName());
        }
        brandRepository.save(brand);
    }

    @Override
    public void delete(UUID uuid) throws BrandNotFoundException {
        var brand = brandRepository.findById(uuid)
                .orElseThrow(() -> new BrandNotFoundException("Brand with the given ID not found."));
        if (!brand.getProducts().isEmpty()) {
            brand.getProducts().forEach(product -> product.setIsDisabled(true));
            brand.setDisabled(true);
            brandRepository.save(brand);
        } else {
            brandRepository.delete(brand);
        }
    }

    @Override
    public void saveToDB(MultipartFile image, String name) throws IOException {
        String newFileName = FileUploadUtil.removeSpecialCharacters(Objects.requireNonNull(image.getOriginalFilename()));
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        FileUploadUtil.saveFile("brand-photos", fileName, image);
        var brand = Brand
                .builder()
                .name(name)
                .picture(newFileName)
                .products(new HashSet<>())
                .isDisabled(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        brandRepository.save(brand);
    }
}
