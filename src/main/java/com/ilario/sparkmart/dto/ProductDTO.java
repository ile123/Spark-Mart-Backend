package com.ilario.sparkmart.dto;

import java.util.UUID;

public record ProductDTO(UUID id, String name,
                         String description, String shortDescription,
                         String specifications, Double price,
                         String imageName, Integer quantity,
                         String brand, String category) {}
