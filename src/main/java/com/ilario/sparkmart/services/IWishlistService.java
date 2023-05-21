package com.ilario.sparkmart.services;

import com.ilario.sparkmart.models.Wishlist;

import java.util.List;
import java.util.UUID;

public interface IWishlistService {
    Wishlist getWishList(UUID id);
    Wishlist getLastWishlist();
    List<Wishlist> getAllWishlists();
    void saveWishList(Wishlist wishlist);
}