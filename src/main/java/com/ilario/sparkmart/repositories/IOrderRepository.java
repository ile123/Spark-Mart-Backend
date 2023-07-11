package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.models.Order;
import com.ilario.sparkmart.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IOrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT COUNT(x) FROM Order x WHERE x.user = :user")
    Integer getTotalAmountOfOrdersByUser(@Param("user")User user);
}
