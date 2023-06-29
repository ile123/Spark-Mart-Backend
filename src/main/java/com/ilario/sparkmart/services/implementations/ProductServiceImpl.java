package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.DisplayProductDTO;
import com.ilario.sparkmart.dto.ProductDTO;
import com.ilario.sparkmart.exceptions.products.ProductNotFoundException;
import com.ilario.sparkmart.mappers.ProductMapper;
import com.ilario.sparkmart.models.Brand;
import com.ilario.sparkmart.models.Product;
import com.ilario.sparkmart.repositories.IBrandRepository;
import com.ilario.sparkmart.repositories.ICategoryRepository;
import com.ilario.sparkmart.repositories.IProductRepository;
import com.ilario.sparkmart.services.IProductService;
import com.ilario.sparkmart.utility.FileUploadUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

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
        var brandsDTO = pageResult
                .getContent()
                .stream()
                .filter(Product::isEnabled)
                .map(productMapper::toProductDTO)
                .toList();
        return new PageImpl<>(brandsDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public Page<DisplayProductDTO> getAllDisplayProducts(int page, int pageSize, String sortBy, String sortDir, String keyword) {
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
        var brandsDTO = pageResult
                .getContent()
                .stream()
                .filter(Product::isEnabled)
                .map(productMapper::toDisplayProductDTO)
                .toList();
        return new PageImpl<>(brandsDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public void update(UUID uuid, ProductDTO entity) {
        if(!productRepository.existsById(uuid)) return;
        var product = productRepository.getReferenceById(uuid);
        var brand = brandRepository.findByName(entity.brand());
        var category = categoryRepository.findByName(entity.category());
        brand.getProducts().add(product);
        category.getProducts().add(product);
        product.setName(entity.name());
        product.setDescription(entity.description());
        product.setShortDescription(entity.shortDescription());
        product.setSpecifications(entity.specifications());
        product.setPrice(entity.price());
        product.setPicture(entity.picture());
        product.setQuantity(entity.quantity());
        productRepository.save(product);
    }



    @Override
    public void delete(UUID uuid) {
        var product = productRepository.findById(uuid);
        if(product.isEmpty()) {
            return;
        }
        productRepository.delete(product.get());
    }

    @Override
    public void saveProductToTheDB(MultipartFile image, String name,
                                   String description, String shortDescription, String specifications,
                                   Double price, Integer quantity,
                                   String brand, String category) throws IOException {
        var product = new Product();
        var brandToSave = brandRepository.findByName(brand.toLowerCase());
        var categoryToSave = categoryRepository.findByName(category.toLowerCase());
        brandToSave.getProducts().add(product);
        categoryToSave.getProducts().add(product);
        product.setName(name);
        product.setDescription(description);
        product.setShortDescription(shortDescription);
        product.setSpecifications(specifications);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setBrand(brandToSave);
        product.setCategory(categoryToSave);
        String newFileName = FileUploadUtil.removeSpecialCharacters(Objects.requireNonNull(image.getOriginalFilename()));
        product.setPicture(newFileName);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        FileUploadUtil.saveFile("product-photos", fileName, image);
        productRepository.save(product);
    }

    @Override
    public Product getProductFromDB(UUID id) {
        return productRepository.getReferenceById(id);
    }
}
