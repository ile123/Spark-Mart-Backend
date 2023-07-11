package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.PurchaseDTO;
import com.ilario.sparkmart.dto.WishlistDTO;
import com.ilario.sparkmart.services.IProductService;
import com.ilario.sparkmart.services.IUserService;
import com.ilario.sparkmart.services.IWishlistService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final IUserService userService;
    private final IProductService productService;
    private final IWishlistService wishlistService;

    public CustomerController(IUserService userService, IProductService productService, IWishlistService wishlistService) {
        this.userService = userService;
        this.productService = productService;
        this.wishlistService = wishlistService;
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
    public ResponseEntity<String> customerPurchaseProducts(@RequestBody PurchaseDTO purchaseDTO) {
        if(purchaseDTO.products().isEmpty()) {
            return new ResponseEntity<>("ERROR: Empty purchase JSON!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Purchase created successfully", HttpStatus.OK);
    }

}
