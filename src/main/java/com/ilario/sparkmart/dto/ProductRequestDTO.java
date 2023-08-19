package com.ilario.sparkmart.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record ProductRequestDTO(
        UUID id, String name, String description,
        String shortDescription, Double price, Integer quantity,
        String specifications, String brand, String category,
        MultipartFile image) {}
