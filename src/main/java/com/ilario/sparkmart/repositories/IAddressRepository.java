package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IAddressRepository extends JpaRepository<Address, UUID> {
    @Query("SELECT x from Address x WHERE x.streetAddress = :streetAddress AND x.city = :city")
    Address findAddressByStreetAddressAndCity(@Param("streetAddress") String streetAddress, @Param("city") String city);
}
