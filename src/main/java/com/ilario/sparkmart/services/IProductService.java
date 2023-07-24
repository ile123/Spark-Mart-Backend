package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.DisplayProductDTO;
import com.ilario.sparkmart.dto.ProductDTO;
import com.ilario.sparkmart.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface IProductService {
    ProductDTO getById(UUID id);
    void saveToDB(MultipartFile image, String name,
                  String description, String shortDescription, String specifications,
                  Double price, Integer quantity,
                  String brand, String category) throws IOException;
    Page<ProductDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword);
    void update(UUID id, ProductDTO entity);
    void delete(UUID id);

    Product getProductFromDB(UUID id);

    Page<DisplayProductDTO> getAllDisplayProducts(int page, int pageSize, String sortBy, String sortDir, String keyword);

    Page<DisplayProductDTO> getAllDisplayProductsByBrand(int page, int pageSize, String sortBy, String sortDir, String keyword, String name);

    Page<DisplayProductDTO> getAllDisplayProductsByCategory(int page, int pageSize, String sortBy, String sortDir, String keyword, String name);
}
