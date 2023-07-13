package com.ilario.sparkmart.mappers;

import com.ilario.sparkmart.dto.OrderDTO;
import com.ilario.sparkmart.models.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderDTO toOrderDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getOrderNO(),
                order.getTotal(),
                order.getOrderDate(),
                order.getShippingDate(),
                order.getOrderStatus());
    }
}
