package com.ilario.sparkmart.dto;

import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record BrandRequestDTO(UUID id, String name, MultipartFile image) {
}
