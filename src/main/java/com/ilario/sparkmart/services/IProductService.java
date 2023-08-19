package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.DisplayProductDTO;
import com.ilario.sparkmart.dto.ProductDTO;
import com.ilario.sparkmart.dto.ProductRequestDTO;
import com.ilario.sparkmart.exceptions.products.ProductNotFoundException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.UUID;

public interface IProductService {
    ProductDTO getById(UUID id) throws ProductNotFoundException;
    void saveToDB(ProductRequestDTO productRequestDTO) throws IOException;
    Page<ProductDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword);
    void update(UUID id, ProductDTO entity) throws ProductNotFoundException;
    void delete(UUID id) throws ProductNotFoundException;
    Page<DisplayProductDTO> getAllDisplayProducts(int page, int pageSize, String sortBy, String sortDir, String keyword);
    Page<DisplayProductDTO> getAllDisplayProductsByBrand(int page, int pageSize, String sortBy, String sortDir, String keyword, String name);
    Page<DisplayProductDTO> getAllDisplayProductsByCategory(int page, int pageSize, String sortBy, String sortDir, String keyword, String name);
}
