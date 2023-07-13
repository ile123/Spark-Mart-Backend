package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.models.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IOrderProductRepository extends JpaRepository<OrderProduct, UUID> {
}
