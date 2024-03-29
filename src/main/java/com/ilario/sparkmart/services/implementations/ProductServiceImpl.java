package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.DisplayProductDTO;
import com.ilario.sparkmart.dto.ProductDTO;
import com.ilario.sparkmart.dto.ProductRequestDTO;
import com.ilario.sparkmart.exceptions.products.ProductNotFoundException;
import com.ilario.sparkmart.mappers.ProductMapper;
import com.ilario.sparkmart.models.Product;
import com.ilario.sparkmart.repositories.IBrandRepository;
import com.ilario.sparkmart.repositories.ICategoryRepository;
import com.ilario.sparkmart.repositories.IProductRepository;
import com.ilario.sparkmart.services.IProductService;
import com.ilario.sparkmart.utility.FileUploadUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final IBrandRepository brandRepository;
    private final ICategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(IProductRepository productRepository, IBrandRepository brandRepository, ICategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDTO getById(UUID uuid) throws ProductNotFoundException {
        var product = productRepository.findById(uuid).orElseThrow(() -> new ProductNotFoundException("ERROR: Product not found by given ID."));
        return productMapper.toProductDTO(product);
    }

    @Override
    public Page<ProductDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword) {
        var pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var pageResult = keyword.isEmpty() ?
                productRepository.findAll(pageable) :
                productRepository.findAllByKeyword(keyword.toLowerCase(), pageable);
        var brandsDTO = pageResult
                .getContent()
                .stream()
                .filter(Product::isEnabled)
                .map(productMapper::toProductDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(brandsDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public Page<DisplayProductDTO> getAllDisplayProducts(int page, int pageSize, String sortBy, String sortDir, String keyword) {
        var pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var pageResult = keyword.isEmpty() ?
                productRepository.findAll(pageable) :
                productRepository.findAllByKeyword(keyword.toLowerCase(), pageable);
        var brandsDTO = pageResult
                .getContent()
                .stream()
                .filter(Product::isEnabled)
                .map(productMapper::toDisplayProductDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(brandsDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public Page<DisplayProductDTO> getAllDisplayProductsByBrand(int page, int pageSize, String sortBy, String sortDir, String keyword, String name) {
        var brand = brandRepository.findByName(name.toLowerCase());
        var pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var pageResult = keyword.isEmpty() ?
                productRepository.findAllByBrand(brand, pageable) :
                productRepository.findAllByKeywordAndBrand(keyword.toLowerCase(), brand, pageable);
        var brandsDTO = pageResult
                .getContent()
                .stream()
                .filter(Product::isEnabled)
                .map(productMapper::toDisplayProductDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(brandsDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public Page<DisplayProductDTO> getAllDisplayProductsByCategory(int page, int pageSize, String sortBy, String sortDir, String keyword, String name) {
        var category = categoryRepository.findByName(name.toLowerCase());
        var pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var pageResult = keyword.isEmpty() ?
                productRepository.findAllByCategory(category, pageable) :
                productRepository.findAllByKeywordAndCategory(keyword.toLowerCase(), category, pageable);
        var brandsDTO = pageResult
                .getContent()
                .stream()
                .filter(Product::isEnabled)
                .map(productMapper::toDisplayProductDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(brandsDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public void update(UUID uuid, ProductDTO entity) throws ProductNotFoundException {
        var product = productRepository
                .findById(uuid)
                .orElseThrow(() -> new ProductNotFoundException("ERROR: Product by given ID not found,"));
        if(!entity.brand().isEmpty()) {
            var brand = brandRepository.findByName(entity.brand());
            brand.getProducts().add(product);
            product.setBrand(brand);
        }
        if(!entity.category().isEmpty()) {
            var category = categoryRepository.findByName(entity.category());
            category.getProducts().add(product);
            product.setCategory(category);
        }
        if(!entity.picture().isEmpty()) {
            product.setPicture(entity.picture());
        }
        product.setName(entity.name());
        product.setDescription(entity.description());
        product.setShortDescription(entity.shortDescription());
        product.setSpecifications(entity.specifications());
        product.setPrice(entity.price());
        product.setQuantity(entity.quantity());
        productRepository.save(product);
    }

    @Override
    public void delete(UUID uuid) throws ProductNotFoundException {
        var product = productRepository
                .findById(uuid)
                .orElseThrow(() -> new ProductNotFoundException("ERROR: Product by given ID not found."));
        if(!product.getOrders().isEmpty() || !product.getWishlists().isEmpty()) {
            product.setIsDisabled(true);
            productRepository.save(product);
        } else {
            productRepository.delete(product);
        }
    }

    @Override
    public void saveToDB(ProductRequestDTO productRequestDTO) throws IOException {
        var brandToSave = brandRepository.findByName(productRequestDTO.brand().toLowerCase());
        var categoryToSave = categoryRepository.findByName(productRequestDTO.category().toLowerCase());
        if(productRequestDTO.image() == null) {
            throw new IOException("ERROR: Image is missing.");
        }
        String newFileName = FileUploadUtil.removeSpecialCharacters(Objects.requireNonNull(productRequestDTO.image().getOriginalFilename()));
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(productRequestDTO.image().getOriginalFilename()));
        FileUploadUtil.saveFile("product-photos", fileName, productRequestDTO.image());
        var product = Product
                .builder()
                .name(productRequestDTO.name())
                .description(productRequestDTO.description())
                .shortDescription(productRequestDTO.shortDescription())
                .specifications(productRequestDTO.specifications())
                .price(productRequestDTO.price())
                .quantity(productRequestDTO.quantity())
                .brand(brandToSave)
                .category(categoryToSave)
                .picture(newFileName)
                .isDisabled(false)
                .wishlists(new HashSet<>())
                .orders(new HashSet<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        brandToSave.getProducts().add(product);
        categoryToSave.getProducts().add(product);
        productRepository.save(product);
    }
}
