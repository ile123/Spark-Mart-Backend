package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.*;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import com.ilario.sparkmart.models.Order;
import com.ilario.sparkmart.models.OrderProduct;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOrderService {
    OrderDTO getById(UUID uuid);
    void savePurchase(PurchaseDTO purchaseDTO) throws UserNotFoundException;
    void changeOrderStatus(UUID orderId);
    void changeOrderProductStatus(UUID orderId, UUID productId);
    Order getLastOrder();
    Optional<OrderProduct> getOrderProductByID(UUID id);
    List<OrderProductDTO> getAllProductsByOrder(UUID orderId);
    Page<OrderDTO> getAllOrdersByUser(UUID userId, int page, int pageSize, String sortBy, String sortDir);
    Page<DisplayProductDTO> getAllWishlistsByUser(UUID userId, int page, int pageSize);
    ProductStatisticsDTO getProductStatistics(UUID productId);
}
