package com.ilario.sparkmart.mappers;

import com.ilario.sparkmart.dto.DisplayProductDTO;
import com.ilario.sparkmart.dto.ProductDTO;
import com.ilario.sparkmart.models.Product;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toProductDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(),
                product.getDescription(), product.getShortDescription(),
                product.getSpecifications(), product.getPrice(),
                product.getPicture(), product.getQuantity(),
                product.getBrand().getName(), product.getCategory().getName());
    }

    public DisplayProductDTO toDisplayProductDTO(Product product) {
        return new DisplayProductDTO(product.getId(), product.getName(), product.getPicture());
    }
}
