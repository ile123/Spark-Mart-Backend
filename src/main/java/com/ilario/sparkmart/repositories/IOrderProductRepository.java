package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.models.Order;
import com.ilario.sparkmart.models.OrderProduct;
import com.ilario.sparkmart.models.Product;
import com.ilario.sparkmart.models.WishlistProduct;
import com.ilario.sparkmart.security.misc.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IOrderProductRepository extends JpaRepository<OrderProduct, UUID> {

    @Query("SELECT x FROM OrderProduct x WHERE x.product = :product")
    List<OrderProduct> getAllOrderProductsByProduct(@Param("product") Product product);

    @Query("SELECT x FROM WishlistProduct x WHERE x.product = :product")
    List<WishlistProduct> getAllWishlistProductsByProduct(@Param("product") Product product);

    @Query("SELECT x FROM OrderProduct x WHERE x.order = :order AND x.isDelivered = false ")
    List<OrderProduct> getAllProductsByOrder(@Param("order") Order order);

    @Query("SELECT x FROM OrderProduct x WHERE x.order = :order AND x.product = :product AND x.isDelivered = false ")
    Optional<OrderProduct> getOrderProductByOrderAndProduct(@Param("order") Order order, @Param("product") Product product);

    @Query("SELECT COUNT(x) FROM User x WHERE x.role = :role")
    Integer getTotalNumberOfCustomers(@Param("role")Role role);
}
