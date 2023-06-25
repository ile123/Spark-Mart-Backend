package com.ilario.sparkmart.mappers;

import com.ilario.sparkmart.dto.BrandDTO;
import com.ilario.sparkmart.models.Brand;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class BrandMapper {
    public BrandDTO toBrandDTO(Brand brand) {
        return new BrandDTO(brand.getId(), brand.getName(), brand.getPicture());
    }
}