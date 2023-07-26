package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.WishlistDTO;
import com.ilario.sparkmart.exceptions.products.ProductNotFoundException;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;

public interface IWishlistService {
    void saveOrRemoveProductWishlist(WishlistDTO wishlistDTO) throws UserNotFoundException, ProductNotFoundException;

    Boolean checkIfWishlistWasAlreadyAdded(WishlistDTO wishlistDTO) throws UserNotFoundException, ProductNotFoundException;
}
