package com.ilario.sparkmart.mappers;

import com.ilario.sparkmart.dto.ProductDTO;
import com.ilario.sparkmart.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductDTO toProductDTO(Product product) {
        return new ProductDTO(
                product.getId(), product.getName(),
                product.getDescription(), product.getShortDescription(),
                product.getSpecifications(), product.getPrice(),
                product.getPicture(), product.getQuantity(),
                product.getBrand().getName(), product.getCategory().getName());
    }
}
