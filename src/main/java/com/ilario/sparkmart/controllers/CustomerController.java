package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.*;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import com.ilario.sparkmart.mappers.OrderMapper;
import com.ilario.sparkmart.models.Order;
import com.ilario.sparkmart.models.WishlistProduct;
import com.ilario.sparkmart.repositories.IUserRepository;
import com.ilario.sparkmart.services.IOrderService;
import com.ilario.sparkmart.services.IProductService;
import com.ilario.sparkmart.services.IUserService;
import com.ilario.sparkmart.services.IWishlistService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
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
        var pageResult = orderService.getAllOrdersByUser(userId, page, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @GetMapping("/wishlists/{userId}")
    public ResponseEntity<Page<DisplayProductDTO>> getAllWishlistsByUser(
    @PathVariable UUID userId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int pageSize) {
        var pageResult = orderService.getAllWishlistsByUser(userId, page, pageSize);
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }

    @PostMapping("/addedToWishlist")
    public ResponseEntity<Boolean> checkIfAlreadyAddedToWishlist(@RequestBody WishlistDTO wishlistDTO) {
        var user = userService.getById(wishlistDTO.userId());
        if(user == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        var product = productService.getProductFromDB(wishlistDTO.productId());
        if(product == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        var result = wishlistService.checkIfWishlistWasAlreadyAdded(wishlistDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/wishlist")
    public ResponseEntity<String> saveOrRemoveUserWishlist(@RequestBody WishlistDTO wishlistDTO) {
        var user = userService.getById(wishlistDTO.userId());
        if(user == null) {
            return new ResponseEntity<>("ERROR: User dose not exist!", HttpStatus.BAD_REQUEST);
        }
        var product = productService.getProductFromDB(wishlistDTO.productId());
        if(product == null) {
            return new ResponseEntity<>("ERROR: Product dose not exist!", HttpStatus.BAD_REQUEST);
        }
        wishlistService.saveOrRemoveProductWishlist(wishlistDTO);
        return new ResponseEntity<>("Wishlist added or removed successfully!", HttpStatus.OK);
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> customerPurchaseProducts(@RequestBody PurchaseDTO purchaseDTO) throws UserNotFoundException {
        if(purchaseDTO.products().isEmpty()) {
            return new ResponseEntity<>("ERROR: Empty purchase JSON!", HttpStatus.BAD_REQUEST);
        }
        orderService.savePurchase(purchaseDTO);
        return new ResponseEntity<>("Purchase created successfully", HttpStatus.OK);
    }

}
