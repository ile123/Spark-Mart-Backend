package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.ProductDTO;
import com.ilario.sparkmart.exceptions.products.ProductNotFoundException;
import com.ilario.sparkmart.mappers.ProductMapper;
import com.ilario.sparkmart.models.Category;
import com.ilario.sparkmart.models.Product;
import com.ilario.sparkmart.repositories.IBrandRepository;
import com.ilario.sparkmart.repositories.ICategoryRepository;
import com.ilario.sparkmart.repositories.IProductRepository;
import com.ilario.sparkmart.services.IProductService;
import com.ilario.sparkmart.utility.FileUploadUtil;
import org.springframework.data.domain.*;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final ProductMapper productMapper;
    private final IBrandRepository brandRepository;
    private final ICategoryRepository categoryRepository;

    public ProductServiceImpl(IProductRepository productRepository, ProductMapper productMapper, IBrandRepository brandRepository, ICategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductDTO getById(UUID uuid) {
        try {
            var product = productRepository.findById(uuid);
            if(product.isEmpty()) {
                throw new ProductNotFoundException("ERROR: Product not found by given ID!");
            }
            return productMapper.toProductDTO(product.get());
        } catch (ProductNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public void saveToDB(ProductDTO entity) {}

    @Override
    public Page<ProductDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Product> pageResult;
        if(keyword.isEmpty()) {
            pageResult = productRepository.findAll(pageable);
        } else {
            pageResult = productRepository.findAllByKeyword(keyword, pageable);
        }
        var categoriesDTO = pageResult
                .getContent()
                .stream()
                .filter(Product::isEnabled)
                .map(productMapper::toProductDTO)
                .toList();
        return new PageImpl<>(categoriesDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public void update(UUID uuid, ProductDTO entity) {
        if(!productRepository.existsById(uuid)) return;
        var category = categoryRepository.getCategoryByName(entity.category());
        var brand = brandRepository.getBrandByName(entity.brand());
        var product = productRepository.getReferenceById(uuid);
        product.setName(entity.name());
        product.setDescription(entity.description());
        product.setShortDescription(entity.shortDescription());
        product.setSpecifications(entity.specifications());
        product.setPrice(entity.price());
        product.setPicture(entity.imageName());
        product.setQuantity(entity.quantity());
        product.setBrand(brand);
        product.setCategory(category);
        product.setPicture(entity.imageName());
        brand.getProducts().add(product);
        category.getProducts().add(product);
        categoryRepository.save(category);
    }

    @Override
    public void delete(UUID uuid) {
        var category = categoryRepository.findById(uuid);
        if(category.isEmpty()) {
            return;
        }
        if(!category.get().getProducts().isEmpty()) {
            for(var product : category.get().getProducts()) {
                product.setIsDisabled(true);
            }
            category.get().setDisabled(true);
            categoryRepository.save(category.get());
            return;
        }
        categoryRepository.delete(category.get());
    }

    @Override
    public void saveProductToTheDB(MultipartFile image, String name,
                                    String description, String shortDescription,
                                    String specifications, Double price, Integer quantity,
                                   String category, String brand) throws IOException {
        String newFileName = FileUploadUtil.removeSpecialCharacters(Objects.requireNonNull(image.getOriginalFilename()));
        var categoryToSave = categoryRepository.getCategoryByName(category);
        var brandToSave = brandRepository.getBrandByName(brand);
        var product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setShortDescription(shortDescription);
        product.setSpecifications(specifications);
        product.setPrice(price);
        product.setPicture(newFileName);
        product.setQuantity(quantity);
        product.setBrand(brandToSave);
        product.setCategory(categoryToSave);
        brandToSave.getProducts().add(product);
        categoryToSave.getProducts().add(product);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        String uploadDir = "src/main/resources/images/product-photos";
        FileUploadUtil.saveFile(uploadDir, fileName, image);
        productRepository.save(product);
    }

    @Override
    public Category getProductFromDB(UUID id) {
        return categoryRepository.getReferenceById(id);
    }
}
