package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.models.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface IBrandRepository extends JpaRepository<Brand, UUID> {
    @Query("SELECT x FROM Brand x WHERE LOWER(x.name) LIKE %:keyword%")
    public Page<Brand> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
