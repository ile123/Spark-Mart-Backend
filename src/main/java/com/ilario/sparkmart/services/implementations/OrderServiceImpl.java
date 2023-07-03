package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.OrderDTO;
import com.ilario.sparkmart.services.IOrderService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {
    @Override
    public OrderDTO getById(UUID uuid) {
        return null;
    }

    @Override
    public void saveToDB(OrderDTO entity) {

    }

    @Override
    public Page<OrderDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword) {
        return null;
    }

    @Override
    public void update(UUID uuid, OrderDTO entity) {}

    @Override
    public void delete(UUID uuid) {}
}
