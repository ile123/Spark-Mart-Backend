package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.*;
import com.ilario.sparkmart.exceptions.orderProducts.OrderProductNotFoundException;
import com.ilario.sparkmart.exceptions.orderProducts.OrderProductsNotFoundException;
import com.ilario.sparkmart.exceptions.orders.OrderNotFoundException;
import com.ilario.sparkmart.exceptions.products.ProductNotFoundException;
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
        try {
            var allOrders = orderService.getAllOrdersByUser(userId, page, pageSize, sortBy, sortDir);
            return ResponseEntity.ok(allOrders);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/wishlists/{userId}")
    public ResponseEntity<Page<DisplayProductDTO>> getAllWishlistsByUser(
    @PathVariable UUID userId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int pageSize) {
        try {
            var allWishlists = orderService.getAllWishlistsByUser(userId, page, pageSize);
            return ResponseEntity.ok(allWishlists);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable UUID orderId) {
        try {
            var order = orderService.getById(orderId);
            return ResponseEntity.ok(order);
        } catch (OrderNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all-products-by-order/{orderId}")
    public ResponseEntity<List<OrderProductDTO>> getAllProductsByOrder(@PathVariable UUID orderId) {
        try {
            var allProducts = orderService.getAllProductsByOrder(orderId);
            return ResponseEntity.ok(allProducts);
        } catch (OrderNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addedToWishlist")
    public ResponseEntity<Boolean> checkIfAlreadyAddedToWishlist(@RequestBody WishlistDTO wishlistDTO) {
        try {
            var result = wishlistService.checkIfWishlistWasAlreadyAdded(wishlistDTO);
            return ResponseEntity.ok(result);
        } catch (UserNotFoundException | ProductNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/wishlist")
    public ResponseEntity<String> saveOrRemoveUserWishlist(@RequestBody WishlistDTO wishlistDTO) {
        try {
            wishlistService.saveOrRemoveProductWishlist(wishlistDTO);
            return ResponseEntity.ok("Wishlist added or removed successfully!");
        } catch (UserNotFoundException | ProductNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> customerPurchaseProducts(@RequestBody PurchaseDTO purchaseDTO) {
        try {
            orderService.savePurchase(purchaseDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Order saved successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/change-order-status/{orderId}")
    public ResponseEntity<String> changeOrderStatus(@PathVariable UUID orderId) {
        try {
            orderService.changeOrderStatus(orderId);
            return ResponseEntity.ok("Order status changed successfully!");
        } catch (OrderNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/change-product-order-status/{orderProductId}")
    public ResponseEntity<String> changeOrderProductStatus(@PathVariable UUID orderProductId) {
        try {
            orderService.changeOrderProductStatus(orderProductId);
            return ResponseEntity.ok("Order status changed successfully");
        } catch (OrderProductNotFoundException | OrderProductsNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
