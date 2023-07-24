package com.ilario.sparkmart.dto;

import java.util.UUID;

public record OrderProductDTO(UUID id, String name, Short amount, String picture, String arrivalDate) {}
