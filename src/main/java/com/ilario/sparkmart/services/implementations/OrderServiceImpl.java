package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.OrderDTO;
import com.ilario.sparkmart.dto.PurchaseDTO;
import com.ilario.sparkmart.models.Order;
import com.ilario.sparkmart.models.Product;
import com.ilario.sparkmart.repositories.IOrderProductRepository;
import com.ilario.sparkmart.repositories.IOrderRepository;
import com.ilario.sparkmart.repositories.IUserRepository;
import com.ilario.sparkmart.services.IOrderService;
import com.ilario.sparkmart.services.IProductService;
import com.ilario.sparkmart.services.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IProductService productService;
    private final IOrderProductRepository orderProductRepository;
    private final IUserRepository userRepository;

    public OrderServiceImpl(IOrderRepository orderRepository, IProductService productService, IOrderProductRepository orderProductRepository, IUserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderProductRepository = orderProductRepository;
        this.userRepository = userRepository;
    }

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

    @Override
    public void savePurchase(PurchaseDTO purchaseDTO) {
        var products = new ArrayList<Product>();
        var total = 0.00;
        var user = userRepository.findById(purchaseDTO.userId());
        if(user.isEmpty()) {
            return;
        }
        for (var productJSON : purchaseDTO.products().entrySet()) {
            products.add(productService.getProductFromDB(productJSON.getKey()));
        }
        var order = new Order();
        order.setOrderNO("ORDER: " + orderRepository.getTotalAmountOfOrdersByUser(user.get()) + 1);
        for(var product: products) {
            total += product.getPrice();
        }
        order.setTotal(total);
        
    }
}
