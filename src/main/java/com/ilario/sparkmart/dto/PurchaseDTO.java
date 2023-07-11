package com.ilario.sparkmart.dto;

import java.util.Map;
import java.util.UUID;

public record PurchaseDTO(UUID userId, Map<UUID, Integer> products) {
}
