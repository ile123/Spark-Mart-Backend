package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.WishlistDTO;

import com.ilario.sparkmart.models.WishlistProduct;
import com.ilario.sparkmart.repositories.IProductRepository;
import com.ilario.sparkmart.repositories.IUserRepository;
import com.ilario.sparkmart.repositories.IWishlistProductRepository;
import com.ilario.sparkmart.repositories.IWishlistRepository;
import com.ilario.sparkmart.services.IWishlistService;
import org.springframework.stereotype.Service;


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
    public void saveOrRemoveProductWishlist(WishlistDTO wishlistDTO) {
        var user = userRepository.findById(wishlistDTO.userId());
        var product = productRepository.findById(wishlistDTO.productId());
        if(user.isEmpty() && product.isEmpty()) {
            return;
        }
        var existingWishlistProduct = wishlistProductRepository.findWishlistProductByWishlistAndProduct(user.get().getWishlist(), product.get());
        if(existingWishlistProduct.isEmpty()) {
            var wishListProduct = WishlistProduct
                    .builder()
                    .wishlist(user.get().getWishlist())
                    .product(product.get())
                    .build();
            user.get().getWishlist().getProducts().add(wishListProduct);
            product.get().getWishlists().add(wishListProduct);
            wishlistProductRepository.save(wishListProduct);
        }
        else {
            user.get().getWishlist().getProducts().remove(existingWishlistProduct.get());
            product.get().getWishlists().remove(existingWishlistProduct.get());
            wishlistRepository.save(user.get().getWishlist());
            productRepository.save(product.get());
            wishlistProductRepository.delete(existingWishlistProduct.get());
        }
    }

    @Override
    public Boolean checkIfWishlistWasAlreadyAdded(WishlistDTO wishlistDTO) {
        var user = userRepository.findById(wishlistDTO.userId());
        var product = productRepository.findById(wishlistDTO.productId());
        return wishlistProductRepository.findWishlistProductByWishlistAndProduct(user.get().getWishlist(), product.get()).isPresent();
    }
}
