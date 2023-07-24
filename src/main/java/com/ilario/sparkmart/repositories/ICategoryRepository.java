package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, UUID> {

    @Query("SELECT x FROM Category x WHERE LOWER(x.name) LIKE %:keyword%")
    Page<Category> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT x FROM Category x WHERE LOWER(x.name) = :keyword")
    Category findByName(@Param("keyword") String keyword);
}
