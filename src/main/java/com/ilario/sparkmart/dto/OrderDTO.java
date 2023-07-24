package com.ilario.sparkmart.dto;

import com.ilario.sparkmart.security.misc.enums.OrderStatus;

import java.util.UUID;

public record OrderDTO(UUID id, String orderNO, Double total, String orderDate, String shippingDate, OrderStatus status) {}
