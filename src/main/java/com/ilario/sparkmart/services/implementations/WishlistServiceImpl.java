package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.WishlistDTO;

import com.ilario.sparkmart.exceptions.products.ProductNotFoundException;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import com.ilario.sparkmart.models.WishlistProduct;
import com.ilario.sparkmart.repositories.IProductRepository;
import com.ilario.sparkmart.repositories.IUserRepository;
import com.ilario.sparkmart.repositories.IWishlistProductRepository;
import com.ilario.sparkmart.repositories.IWishlistRepository;
import com.ilario.sparkmart.services.IWishlistService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class WishlistServiceImpl implements IWishlistService {

    private final IWishlistRepository wishlistRepository;
    private final IUserRepository userRepository;
    private final IWishlistProductRepository wishlistProductRepository;
    private final IProductRepository productRepository;

    public WishlistServiceImpl(IWishlistRepository wishlistRepository, IUserRepository userRepository, IWishlistProductRepository wishlistProductRepository, IProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.wishlistProductRepository = wishlistProductRepository;
        this.productRepository = productRepository;
    }
    @Override
    public void saveOrRemoveProductWishlist(WishlistDTO wishlistDTO) throws UserNotFoundException, ProductNotFoundException {
        var user = userRepository
                .findById(wishlistDTO.userId())
                .orElseThrow(() -> new UserNotFoundException("ERROR: User by given ID not found."));
        var product = productRepository
                .findById(wishlistDTO.productId())
                .orElseThrow(() -> new ProductNotFoundException("ERROR: Product by given ID not found."));
        var existingWishlistProduct = wishlistProductRepository.findWishlistProductByWishlistAndProduct(user.getWishlist(), product);
        existingWishlistProduct.ifPresentOrElse(wishlistProduct -> {
            user.getWishlist().getProducts().remove(wishlistProduct);
            product.getWishlists().remove(wishlistProduct);
            wishlistProductRepository.delete(wishlistProduct);
        }, () -> {
            var wishlistProduct = WishlistProduct.builder()
                    .wishlist(user.getWishlist())
                    .product(product)
                    .createdAt(LocalDateTime.now())
                    .build();
            user.getWishlist().getProducts().add(wishlistProduct);
            product.getWishlists().add(wishlistProduct);
            wishlistProductRepository.save(wishlistProduct);
        });
        wishlistRepository.save(user.getWishlist());
        productRepository.save(product);
    }

    @Override
    public Boolean checkIfWishlistWasAlreadyAdded(WishlistDTO wishlistDTO) throws UserNotFoundException, ProductNotFoundException {
        var user = userRepository
                .findById(wishlistDTO.userId())
                .orElseThrow(() -> new UserNotFoundException("ERROR: User by given ID not found."));
        var product = productRepository
                .findById(wishlistDTO.productId())
                .orElseThrow(() -> new ProductNotFoundException("ERROR: Product by given ID not found."));
        return wishlistProductRepository.findWishlistProductByWishlistAndProduct(user.getWishlist(), product).isPresent();
    }
}
