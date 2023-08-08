package com.ilario.sparkmart.dto;

import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record CategoryRequestDTO(UUID id, String name, String description, MultipartFile image) {
}
