package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.*;
import com.ilario.sparkmart.exceptions.orderProducts.OrderProductNotFoundException;
import com.ilario.sparkmart.exceptions.orderProducts.OrderProductsNotFoundException;
import com.ilario.sparkmart.exceptions.orders.OrderNotFoundException;
import com.ilario.sparkmart.exceptions.products.ProductNotFoundException;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import com.ilario.sparkmart.mappers.OrderMapper;
import com.ilario.sparkmart.mappers.ProductMapper;
import com.ilario.sparkmart.models.Order;
import com.ilario.sparkmart.models.OrderProduct;
import com.ilario.sparkmart.models.Product;
import com.ilario.sparkmart.repositories.*;
import com.ilario.sparkmart.security.misc.enums.OrderStatus;
import com.ilario.sparkmart.security.misc.enums.Role;
import com.ilario.sparkmart.services.IOrderService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    public OrderDTO getById(UUID uuid) throws OrderNotFoundException {
        var order = orderRepository.findById(uuid).orElseThrow(() -> new OrderNotFoundException("ERROR: Order by given ID not found."));
        return orderMapper.toOrderDTO(order);
    }

    @Override
    @Transient
    public void savePurchase(PurchaseDTO purchaseDTO) throws UserNotFoundException {
        var products = new ArrayList<Product>();
        var user = userRepository.findById(purchaseDTO.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        for (var productJSON : purchaseDTO.products().entrySet()) {
            var product = productRepository.findById(productJSON.getKey());
            product.ifPresent(products::add);
        }
        var total = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        var order = Order
                .builder()
                .orderNO("ORDER: " + (orderRepository.getTotalAmountOfOrdersByUser(user) + 1))
                .total(total)
                .user(user)
                .products(new HashSet<>())
                .isComplete(false)
                .orderDate(LocalDateTime.now())
                .shippingDate(LocalDateTime.now().plusDays(1))
                .orderStatus(OrderStatus.PENDING)
                .arrivalDate(LocalDateTime.now().plusDays(14))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        user.getOrders().add(order);
        for(var product: products) {
            var wishlistProduct = wishlistProductRepository.findWishlistProductByWishlistAndProduct(user.getWishlist(), product);
            wishlistProduct.ifPresent(wishlistProductRepository::delete);
            product.setQuantity(product.getQuantity() - purchaseDTO.products().get(product.getId()));
            var orderProduct = OrderProduct
                    .builder()
                    .order(order)
                    .product(product)
                    .quantity(purchaseDTO.products().get(product.getId()))
                    .dateOfDelivery(LocalDateTime.now().plusDays(14))
                    .isDelivered(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            product.getOrders().add(orderProduct);
            order.getProducts().add(orderProduct);
            orderProductRepository.save(orderProduct);
        }
        orderRepository.save(order);
    }

    @Override
    public void changeOrderStatus(UUID orderId) throws OrderNotFoundException {
        var order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("ERROR: Order not found by given ID."));
        switch (order.getOrderStatus()) {
            case PENDING -> order.setOrderStatus(OrderStatus.PROCESSING);
            case PROCESSING -> order.setOrderStatus(OrderStatus.SHIPPED);
        }
        if(order.getOrderStatus().equals(OrderStatus.SHIPPED)) {
            order.setShippingDate(LocalDateTime.now());
        }
        orderRepository.save(order);
    }

    @Override
    public void changeOrderProductStatus(UUID orderProductID) throws OrderProductNotFoundException, OrderProductsNotFoundException {
        var orderProduct = orderProductRepository
                .findById(orderProductID)
                .orElseThrow(() -> new OrderProductNotFoundException("ERROR: Order product by given ID not found."));
        var order = orderProduct.getOrder();
        orderProduct.setIsDelivered(true);
        orderProductRepository.save(orderProduct);
        var allOrderProducts = orderProductRepository.getAllProductsByOrder(order);
        var allDelivered = allOrderProducts
                .stream()
                .allMatch(OrderProduct::getIsDelivered);
        if (allDelivered) {
            order.setOrderStatus(OrderStatus.DELIVERED);
            order.setArrivalDate(LocalDateTime.now());
            order.setIsComplete(true);
            orderRepository.save(order);
        }
    }

    @Override
    public List<OrderProductDTO> getAllProductsByOrder(UUID orderId) throws OrderNotFoundException {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("ERROR: Order by given ID not found."));
        var products = orderProductRepository.getAllProductsByOrder(order);
        return products.isEmpty()
                ? new ArrayList<>()
                : products.stream().map(orderMapper::toOrderProductDTO).collect(Collectors.toList());
    }

    @Override
    public Page<OrderDTO> getAllOrdersByUser(UUID userId, int page, int pageSize, String sortBy, String sortDir) throws UserNotFoundException {
        var pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("ERROR: User by given ID not found."));
        var pageResult = orderRepository.getAllOrdersByUser(user, pageable);
        var ordersDTO = pageResult
                .stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(ordersDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public Page<DisplayProductDTO> getAllWishlistsByUser(UUID userId, int page, int pageSize) throws UserNotFoundException {
        var pageable = PageRequest.of(
                page,
                pageSize);
        var user = userRepository.
                findById(userId)
                .orElseThrow(() -> new UserNotFoundException("ERROR: User not found by given ID."));
        var pageResult= wishlistProductRepository.getAllWishlistsByUser(user.getWishlist(), pageable);
        var productsDTO = pageResult
                .stream()
                .map(x-> productMapper.toDisplayProductDTO(x.getProduct()))
                .collect(Collectors.toList());
        return new PageImpl<>(productsDTO, pageable, pageResult.getTotalElements());
    }

    @Override
    public ProductStatisticsDTO getProductStatistics(UUID productId) throws ProductNotFoundException {
        var product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("ERROR: Product by given ID not found."));
        var allOrderProducts = orderProductRepository.getAllOrderProductsByProduct(product);
        var allWishlistProducts = orderProductRepository.getAllWishlistProductsByProduct(product);
        var totalNumberOfCustomers = orderProductRepository.getTotalNumberOfCustomers(Role.CUSTOMER);
        if(allOrderProducts.isEmpty()) return new ProductStatisticsDTO(product.getQuantity(), 0, 0, allWishlistProducts.size(), totalNumberOfCustomers);
        var totalSold = allOrderProducts
                .stream()
                .mapToInt(OrderProduct::getQuantity)
                .sum();
        var totalProfit = (int)(totalSold * product.getPrice());
        return new ProductStatisticsDTO(product.getQuantity(), totalSold, totalProfit, allWishlistProducts.size(), totalNumberOfCustomers);
    }
}
