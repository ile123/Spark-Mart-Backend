package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.DisplayProductDTO;
import com.ilario.sparkmart.dto.ProductDTO;
import com.ilario.sparkmart.models.Brand;
import com.ilario.sparkmart.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface IProductService extends IBaseService<ProductDTO, UUID> {
    void saveProductToTheDB(MultipartFile image, String name,
                            String description, String shortDescription, String specifications,
                            Double price, Integer quantity,
                            String brand, String category) throws IOException;

    Product getProductFromDB(UUID id);

    Page<DisplayProductDTO> getAllDisplayProducts(int page, int pageSize, String sortBy, String sortDir, String keyword);
}
