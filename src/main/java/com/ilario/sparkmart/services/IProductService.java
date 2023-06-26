package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.ProductDTO;
import com.ilario.sparkmart.models.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface IProductService extends IBaseService<ProductDTO, UUID> {
    void saveProductToTheDB(MultipartFile image, String name,
                             String description, String shortDescription,
                             String specifications, Double price, Integer quantity,
                            String category, String brand) throws IOException;
    Category getProductFromDB(UUID id);
}
