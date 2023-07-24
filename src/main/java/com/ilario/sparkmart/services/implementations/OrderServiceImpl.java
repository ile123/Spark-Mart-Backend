package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.*;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import com.ilario.sparkmart.mappers.OrderMapper;
import com.ilario.sparkmart.mappers.ProductMapper;
import com.ilario.sparkmart.models.Order;
import com.ilario.sparkmart.models.OrderProduct;
import com.ilario.sparkmart.models.Product;
import com.ilario.sparkmart.models.WishlistProduct;
import com.ilario.sparkmart.repositories.*;
import com.ilario.sparkmart.security.misc.enums.OrderStatus;
import com.ilario.sparkmart.services.IOrderService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.*;

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
        var order = orderRepository.findById(uuid);
        return order.map(orderMapper::toOrderDTO).orElse(null);
    }

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
        order.setOrderNO("ORDER: " + (orderRepository.getTotalAmountOfOrdersByUser(user) + 1));
        var total = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        order.setTotal(total);
        order.setUser(user);
        user.getOrders().add(order);
        for(var product: products) {
            var orderProduct = new OrderProduct();
            var wishlistProduct = wishlistProductRepository.findWishlistProductByWishlistAndProduct(user.getWishlist(), product);
            wishlistProduct.ifPresent(wishlistProductRepository::delete);
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
    public void changeOrderStatus(UUID orderId) {
        var order = orderRepository.findById(orderId);
        if(order.isEmpty()) {
            return;
        }
        switch (order.get().getOrderStatus()) {
            case PENDING -> order.get().setOrderStatus(OrderStatus.PROCESSING);
            case PROCESSING -> order.get().setOrderStatus(OrderStatus.SHIPPED);
        }
        orderRepository.save(order.get());
    }

    @Override
    public void changeOrderProductStatus(UUID orderId, UUID productId) {
        var order = orderRepository.findById(orderId);
        var product = productRepository.findById(productId);
        if(order.isEmpty() || product.isEmpty()) {
            return;
        }
        var orderProduct = orderProductRepository.getOrderProductByOrderAndProduct(order.get(), product.get());
        if(orderProduct.isEmpty()) {
            return;
        }
        orderProduct.get().setIsDelivered(true);
        orderProductRepository.save(orderProduct.get());
        var allOrderProducts = orderProductRepository.getAllProductsByOrder(order.get());
        boolean flag = true;
        for(var item: allOrderProducts) {
            if(!item.getIsDelivered()) {
                flag = false;
                break;
            }
        }
        if(flag) {
            order.get().setOrderStatus(OrderStatus.DELIVERED);
            order.get().setIsComplete(true);
            orderRepository.save(order.get());
        }
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
    public Optional<OrderProduct> getOrderProductByID(UUID id) {
        return orderProductRepository.findById(id);
    }

    @Override
    public List<OrderProductDTO> getAllProductsByOrder(UUID orderId) {
        var order = orderRepository.findById(orderId);
        if(order.isEmpty()) {
            return new ArrayList<>();
        }
        var products = orderProductRepository.getAllProductsByOrder(order.get());
        return products.stream().map(orderMapper::toOrderProductDTO).toList();
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

    @Override
    public ProductStatisticsDTO getProductStatistics(UUID productId) {
        var product = productRepository.findById(productId);
        if(product.isEmpty()) {
            return new ProductStatisticsDTO(0,0, 0);
        }
        var totalSold = orderProductRepository.getTotalSoldFromProduct(product.get());
        var totalProfit = (int)(totalSold * product.get().getPrice());
        return new ProductStatisticsDTO(product.get().getQuantity(), totalSold, totalProfit);
    }
}
