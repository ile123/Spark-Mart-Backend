package com.ilario.sparkmart.dto;

import java.util.UUID;

public record CategoryDTO(UUID id, String name, String description, String imageName) {
}
