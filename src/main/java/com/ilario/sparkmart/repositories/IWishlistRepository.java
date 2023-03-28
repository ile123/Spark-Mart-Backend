package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IWishlistRepository extends JpaRepository<Wishlist, UUID> {
}
