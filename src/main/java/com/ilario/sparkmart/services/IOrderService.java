package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.OrderDTO;
import com.ilario.sparkmart.dto.PurchaseDTO;
import org.springframework.data.relational.core.sql.In;

import java.util.Map;
import java.util.UUID;

public interface IOrderService extends IBaseService<OrderDTO, UUID> {
    void savePurchase(PurchaseDTO purchaseDTO);
}
