package com.ilario.sparkmart.mappers;

import com.ilario.sparkmart.dto.BrandDTO;
import com.ilario.sparkmart.dto.DisplayBrandDTO;
import com.ilario.sparkmart.models.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {
    public BrandDTO toBrandDTO(Brand brand) {
        return new BrandDTO(brand.getId(), brand.getName(), brand.getPicture());
    }

    public DisplayBrandDTO toDisplayBrandDTO(Brand brand) {
        return new DisplayBrandDTO(brand.getId(), brand.getName(), brand.getPicture());
    }
}
