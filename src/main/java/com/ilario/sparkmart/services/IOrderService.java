package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.*;
import com.ilario.sparkmart.exceptions.orderProducts.OrderProductNotFoundException;
import com.ilario.sparkmart.exceptions.orderProducts.OrderProductsNotFoundException;
import com.ilario.sparkmart.exceptions.orders.OrderNotFoundException;
import com.ilario.sparkmart.exceptions.products.ProductNotFoundException;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface IOrderService {
    OrderDTO getById(UUID uuid) throws OrderNotFoundException;
    void savePurchase(PurchaseDTO purchaseDTO) throws UserNotFoundException;
    void changeOrderStatus(UUID orderId) throws OrderNotFoundException;
    void changeOrderProductStatus(UUID orderProductID) throws OrderProductNotFoundException, OrderProductsNotFoundException;

    List<OrderProductDTO> getAllProductsByOrder(UUID orderId) throws OrderNotFoundException;
    Page<OrderDTO> getAllOrdersByUser(UUID userId, int page, int pageSize, String sortBy, String sortDir) throws UserNotFoundException;
    Page<DisplayProductDTO> getAllWishlistsByUser(UUID userId, int page, int pageSize) throws UserNotFoundException;
    ProductStatisticsDTO getProductStatistics(UUID productId) throws ProductNotFoundException;
}
