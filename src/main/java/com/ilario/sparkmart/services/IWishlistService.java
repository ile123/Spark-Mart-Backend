package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.WishlistDTO;

public interface IWishlistService {
    void saveOrRemoveProductWishlist(WishlistDTO wishlistDTO);

    Boolean checkIfWishlistWasAlreadyAdded(WishlistDTO wishlistDTO);
}
