package com.ilario.sparkmart.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/images")
public class ImageController {

    @GetMapping("/brand/{imageName}")
    public ResponseEntity<byte[]> GetBrandImage(@PathVariable String imageName) throws IOException {
        Resource resource = new ClassPathResource("images/brand-photos/" + imageName);
        System.out.println(resource);
        byte[] imageBytes = Files.readAllBytes(Path.of(resource.getURI()));
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    @GetMapping("/category/{imageName}")
    public ResponseEntity<byte[]> GetCategoryImage(@PathVariable String imageName) throws IOException {
        Resource resource = new ClassPathResource("images/category-photos/" + imageName);
        byte[] imageBytes = Files.readAllBytes(Path.of(resource.getURI()));
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    @GetMapping("/product/{imageName}")
    public ResponseEntity<byte[]> GetProductImage(@PathVariable String imageName) throws IOException {
        Resource resource = new ClassPathResource("images/product-photos/" + imageName);
        byte[] imageBytes = Files.readAllBytes(Path.of(resource.getURI()));
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }
}
