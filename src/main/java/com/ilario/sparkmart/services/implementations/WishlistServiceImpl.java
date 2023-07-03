package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.WishlistDTO;
import com.ilario.sparkmart.models.Wishlist;
import com.ilario.sparkmart.repositories.IWishlistRepository;
import com.ilario.sparkmart.services.IWishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements IWishlistService {

    private final IWishlistRepository wishlistRepository;

    public WishlistServiceImpl(IWishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public Wishlist getWishList(UUID id) {
        var wishlist = wishlistRepository.findById(id);
        return wishlist.orElse(null);
    }

    @Override
    public Wishlist getLastWishlist() {
        var wishlists = wishlistRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Wishlist::getCreatedAt))
                .toList();
        return wishlists.get(wishlists.size() - 1);
    }

    @Override
    public List<Wishlist> getAllWishlists() {
        return wishlistRepository.findAll();
    }

    @Override
    public void saveWishList(Wishlist wishlist) {
        wishlistRepository.save(wishlist);
    }

    @Override
    public void saveProductWishlist(WishlistDTO wishlistDTO) {

    }
}
