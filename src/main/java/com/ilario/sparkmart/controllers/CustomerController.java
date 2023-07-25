package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.*;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import com.ilario.sparkmart.mappers.OrderMapper;
import com.ilario.sparkmart.repositories.IUserRepository;
import com.ilario.sparkmart.services.IOrderService;
import com.ilario.sparkmart.services.IProductService;
import com.ilario.sparkmart.services.IUserService;
import com.ilario.sparkmart.services.IWishlistService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final IUserService userService;
    private final IProductService productService;
    private final IWishlistService wishlistService;
    private final IOrderService orderService;
    private final IUserRepository userRepository;
    private final OrderMapper orderMapper;

    public CustomerController(IUserService userService, IProductService productService, IWishlistService wishlistService, IOrderService orderService, IUserRepository userRepository, OrderMapper orderMapper) {
        this.userService = userService;
        this.productService = productService;
        this.wishlistService = wishlistService;
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity<Page<OrderDTO>> getAllOrdersByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "orderNO") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        var allOrders = orderService.getAllOrdersByUser(userId, page, pageSize, sortBy, sortDir);
        return allOrders.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(allOrders);
    }

    @GetMapping("/wishlists/{userId}")
    public ResponseEntity<Page<DisplayProductDTO>> getAllWishlistsByUser(
    @PathVariable UUID userId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int pageSize) {
        var allWishlists = orderService.getAllWishlistsByUser(userId, page, pageSize);
        return allWishlists.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(allWishlists);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable UUID orderId) {
        var order = Optional.of(orderService.getById(orderId));
        if(order.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return order.map(x-> ResponseEntity.ok(x))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all-products-by-order/{orderId}")
    public ResponseEntity<List<OrderProductDTO>> getAllProductsByOrder(@PathVariable UUID orderId) {
        var order = Optional.of(orderService.getById(orderId));
        if(order.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var allProducts = orderService.getAllProductsByOrder(orderId);
        return allProducts.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(allProducts);
    }

    @PostMapping("/addedToWishlist")
    public ResponseEntity<Boolean> checkIfAlreadyAddedToWishlist(@RequestBody WishlistDTO wishlistDTO) {
        var user = Optional.of(userService.getById(wishlistDTO.userId()));
        if(user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var product = Optional.of(productService.getById(wishlistDTO.productId()));
        if(product.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var result = wishlistService.checkIfWishlistWasAlreadyAdded(wishlistDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/wishlist")
    public ResponseEntity<String> saveOrRemoveUserWishlist(@RequestBody WishlistDTO wishlistDTO) {
        var user = Optional.of(userService.getById(wishlistDTO.userId()));
        if(user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var product = Optional.of(productService.getProductFromDB(wishlistDTO.productId()));
        if(product.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        wishlistService.saveOrRemoveProductWishlist(wishlistDTO);
        return ResponseEntity.ok("Wishlist added or removed successfully!");
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> customerPurchaseProducts(@RequestBody PurchaseDTO purchaseDTO) throws UserNotFoundException {
        if(purchaseDTO.products().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        orderService.savePurchase(purchaseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order saved successfully");
    }

    @PatchMapping("/change-order-status/{orderId}")
    public ResponseEntity<String> changeOrderStatus(@PathVariable UUID orderId) {
        var order = Optional.of(orderService.getById(orderId));
        if(order.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        orderService.changeOrderStatus(orderId);
        return ResponseEntity.ok("Order status changed successfully!");
    }

    @PatchMapping("/change-product-order-status/{orderProductId}")
    public ResponseEntity<String> changeOrderProductStatus(@PathVariable UUID orderProductId) {
        var orderProduct = orderService.getOrderProductByID(orderProductId);
        if(orderProduct.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        orderService.changeOrderProductStatus(orderProduct.get().getOrder().getId(), orderProduct.get().getProduct().getId());
        return ResponseEntity.ok("Order status changed successfully");
    }

}
