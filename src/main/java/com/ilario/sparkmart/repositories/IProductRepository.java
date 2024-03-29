package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.models.Brand;
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
    Page<Product> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT x FROM Product x WHERE LOWER(x.name) LIKE %:keyword% AND x.brand = :brand")
    Page<Product> findAllByKeywordAndBrand(@Param("keyword") String keyword,
                                           @Param("brand") Brand brand,
                                           Pageable pageable);

    @Query("SELECT x FROM Product x WHERE LOWER(x.name) LIKE %:keyword% AND x.category = :category")
    Page<Product> findAllByKeywordAndCategory(@Param("keyword") String keyword,
                                           @Param("category") Category category,
                                           Pageable pageable);

    @Query("SELECT x FROM Product x WHERE x.brand = :brand")
    Page<Product> findAllByBrand(@Param("brand") Brand brand, Pageable pageable);

    @Query("SELECT x FROM Product x WHERE x.category = :category")
    Page<Product> findAllByCategory(@Param("category") Category category, Pageable pageable);

}
