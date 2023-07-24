package com.ilario.sparkmart.dto;

import java.util.UUID;

public record ChangeProductOrderStatusDTO(UUID orderId, UUID productId) {
}
