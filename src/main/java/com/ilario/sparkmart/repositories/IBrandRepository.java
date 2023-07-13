package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.models.Brand;
import com.ilario.sparkmart.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IBrandRepository extends JpaRepository<Brand, UUID> {
    @Query("SELECT x FROM Brand x WHERE LOWER(x.name) LIKE %:keyword%")
    Page<Brand> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT x FROM Brand x WHERE LOWER(x.name) = :keyword")
    Brand findByName(@Param("keyword") String keyword);
}
