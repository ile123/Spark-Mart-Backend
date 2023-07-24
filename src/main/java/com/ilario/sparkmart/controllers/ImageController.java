package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.utility.FileUploadUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@RequestMapping("/images")
public class ImageController {

    private Resource getResource(String imageName, String uploadDir) throws IOException {
        File photoDir = new File(Objects.requireNonNull(FileUploadUtil.class.getResource("/")).getPath())
                .getParentFile()
                .getParentFile()
                .getParentFile();

        Path pathToPicture = Paths.get(photoDir.getAbsolutePath() + File.separator + "SparkMartPhotos" + File.separator + uploadDir);
        File imageFile = new File(pathToPicture.toAbsolutePath() + File.separator + imageName);
        return new FileSystemResource(imageFile.getAbsolutePath());
    }

    @GetMapping("/category/{imageName}")
    public ResponseEntity<byte[]> GetCategoryImage(@PathVariable String imageName) throws IOException {
        var resource = getResource(imageName, "category-photos");
        byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    @GetMapping("/brand/{imageName}")
    public ResponseEntity<byte[]> GetBrandImage(@PathVariable String imageName) throws IOException {
        var resource = getResource(imageName, "brand-photos");
        byte[] imageBytes = Files.readAllBytes(Path.of(resource.getURI()));
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }

    @GetMapping("/product/{imageName}")
    public ResponseEntity<byte[]> GetProductImage(@PathVariable String imageName) throws IOException {
        var resource = getResource(imageName, "product-photos");
        byte[] imageBytes = Files.readAllBytes(Path.of(resource.getURI()));
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }
}
