package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.DisplayProductDTO;
import com.ilario.sparkmart.dto.OrderDTO;
import com.ilario.sparkmart.dto.ProductDTO;
import com.ilario.sparkmart.dto.PurchaseDTO;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import com.ilario.sparkmart.mappers.OrderMapper;
import com.ilario.sparkmart.mappers.ProductMapper;
import com.ilario.sparkmart.models.Order;
import com.ilario.sparkmart.models.OrderProduct;
import com.ilario.sparkmart.models.Product;
import com.ilario.sparkmart.models.WishlistProduct;
import com.ilario.sparkmart.repositories.*;
import com.ilario.sparkmart.services.IOrderService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final IOrderProductRepository orderProductRepository;
    private final IUserRepository userRepository;
    private final IWishlistProductRepository wishlistProductRepository;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

    public OrderServiceImpl(IOrderRepository orderRepository, IProductRepository productRepository, IOrderProductRepository orderProductRepository, IUserRepository userRepository, IWishlistProductRepository wishlistProductRepository, OrderMapper orderMapper, ProductMapper productMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
        this.userRepository = userRepository;
        this.wishlistProductRepository = wishlistProductRepository;
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
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
    @Transient
    public void savePurchase(PurchaseDTO purchaseDTO) throws UserNotFoundException {
        var products = new ArrayList<Product>();
        var user = userRepository.findById(purchaseDTO.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        for (var productJSON : purchaseDTO.products().entrySet()) {
            var product = productRepository.findById(productJSON.getKey());
            if(product.isEmpty()) {
                return;
            }
            products.add(product.get());
        }
        var order = new Order();
        order.setOrderNO("ORDER: " + orderRepository.getTotalAmountOfOrdersByUser(user) + 1);
        var total = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        order.setTotal(total);
        order.setUser(user);
        user.getOrders().add(order);
        for(var product: products) {
            var orderProduct = new OrderProduct();
            var wishlistProduct = wishlistProductRepository.findWishlistProductByWishlistAndProduct(user.getWishlist(), product);
            if(wishlistProduct.isPresent()) {
                wishlistProductRepository.delete(wishlistProduct.get());
            }
            product.setQuantity(product.getQuantity() - purchaseDTO.products().get(product.getId()));
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(purchaseDTO.products().get(product.getId()));
            product.getOrders().add(orderProduct);
            order.getProducts().add(orderProduct);
            orderProductRepository.save(orderProduct);
        }
        orderRepository.save(order);
    }

    @Override
    public Order getLastOrder() {
        var orders = orderRepository.findAll().stream().sorted(Comparator.comparing(Order::getCreatedAt)).toList();
        if(orders.isEmpty()) {
            return new Order();
        }
        return orders.get(orders.size() - 1);
    }

    @Override
    public Page<OrderDTO> getAllOrdersByUser(UUID userId, int page, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var user = userRepository.findById(userId);
        if(user.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        Page<Order> pageResult = orderRepository.getAllOrdersByUser(user.get(), pageable);
        var ordersDTO = pageResult
                .stream()
                .map(orderMapper::toOrderDTO)
                .toList();
        return new PageImpl<>(ordersDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public Page<DisplayProductDTO> getAllWishlistsByUser(UUID userId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize);
        var user = userRepository.findById(userId);
        if(user.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        Page<WishlistProduct> pageResult= wishlistProductRepository.getAllWishlistsByUser(user.get().getWishlist(), pageable);
        var productsDTO = pageResult
                .stream()
                .map(x-> productMapper.toDisplayProductDTO(x.getProduct()))
                .toList();
        return new PageImpl<>(productsDTO, pageable, pageResult.getTotalElements());
    }
}
