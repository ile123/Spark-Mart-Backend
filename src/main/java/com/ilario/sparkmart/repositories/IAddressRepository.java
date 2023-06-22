package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.models.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IAddressRepository extends JpaRepository<Address, UUID> {
    @Query("SELECT x from Address x WHERE x.streetAddress = :streetAddress AND x.city = :city")
    Optional<Address> findAddressByStreetAddressAndCity(@Param("streetAddress") String streetAddress, @Param("city") String city);

    @Query("SELECT x FROM Address x WHERE LOWER(x.streetAddress) LIKE %:keyword% OR LOWER(x.city) LIKE %:keyword% OR LOWER(x.postalCode) LIKE %:keyword% OR LOWER(x.province) LIKE %:keyword% OR LOWER(x.country) LIKE %:keyword%")
    public Page<Address> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
