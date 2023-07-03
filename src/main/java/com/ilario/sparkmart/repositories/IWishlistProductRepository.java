package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.models.Product;
import com.ilario.sparkmart.models.Wishlist;
import com.ilario.sparkmart.models.WishlistProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IWishlistProductRepository extends JpaRepository<WishlistProduct, UUID> {

    @Query("SELECT x FROM WishlistProduct x WHERE x.wishlist = :wishlist AND x.product = :product")
    Optional<WishlistProduct> findWishlistProductByWishlistAndProduct(@Param("wishlist")Wishlist wishlist, @Param("product")Product product);
}
