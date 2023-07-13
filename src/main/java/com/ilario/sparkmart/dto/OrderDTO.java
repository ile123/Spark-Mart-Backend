package com.ilario.sparkmart.dto;

import com.ilario.sparkmart.security.misc.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderDTO(UUID id, String orderNO, Double total, LocalDateTime orderDate, LocalDateTime shippingDate, OrderStatus status) {}
