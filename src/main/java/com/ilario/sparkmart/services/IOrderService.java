package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.*;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import com.ilario.sparkmart.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.relational.core.sql.In;

import java.util.Map;
import java.util.UUID;

public interface IOrderService extends IBaseService<OrderDTO, UUID> {
    void savePurchase(PurchaseDTO purchaseDTO) throws UserNotFoundException;
    Order getLastOrder();
    Page<OrderDTO> getAllOrdersByUser(UUID userId, int page, int pageSize, String sortBy, String sortDir);
    Page<DisplayProductDTO> getAllWishlistsByUser(UUID userId, int page, int pageSize);
}
