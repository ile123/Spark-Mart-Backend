package com.ilario.sparkmart.dto;

import jakarta.persistence.Column;

import java.util.UUID;

public record ProductDTO(
        UUID id, String name,
        String description, String shortDescription,
        String specifications, Double price,
        String picture, Integer quantity,
        String brand, String category) {
}
