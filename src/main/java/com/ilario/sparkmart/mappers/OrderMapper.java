package com.ilario.sparkmart.mappers;

import com.ilario.sparkmart.dto.OrderDTO;
import com.ilario.sparkmart.dto.OrderProductDTO;
import com.ilario.sparkmart.models.Order;
import com.ilario.sparkmart.models.OrderProduct;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class OrderMapper {

    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public OrderDTO toOrderDTO(Order order) {

        return new OrderDTO(
                order.getId(),
                order.getOrderNO(),
                order.getTotal(),
                order.getOrderDate().format(format),
                order.getShippingDate().format(format),
                order.getOrderStatus());
    }

    public OrderProductDTO toOrderProductDTO(OrderProduct product) {
        return new OrderProductDTO(
                product.getId(),
                product.getProduct().getName(),
                product.getQuantity(),
                product.getProduct().getPicture(),
                product.getDateOfDelivery().format(format));
    }
}
