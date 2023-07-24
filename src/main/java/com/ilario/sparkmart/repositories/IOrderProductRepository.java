package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.models.Order;
import com.ilario.sparkmart.models.OrderProduct;
import com.ilario.sparkmart.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IOrderProductRepository extends JpaRepository<OrderProduct, UUID> {

    @Query("SELECT SUM(x.quantity) FROM OrderProduct x WHERE x.product = :product")
    Integer getTotalSoldFromProduct(@Param("product") Product product);

    @Query("SELECT x FROM OrderProduct x WHERE x.order = :order AND x.isDelivered = false ")
    List<OrderProduct> getAllProductsByOrder(@Param("order") Order order);

    @Query("SELECT x FROM OrderProduct x WHERE x.order = :order AND x.product = :product AND x.isDelivered = false ")
    Optional<OrderProduct> getOrderProductByOrderAndProduct(@Param("order") Order order, @Param("product") Product product);
}
