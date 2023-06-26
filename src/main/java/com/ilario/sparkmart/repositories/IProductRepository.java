package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.models.Category;
import com.ilario.sparkmart.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT x FROM Product x WHERE LOWER(x.name) LIKE %:keyword%")
    public Page<Product> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
