package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
//Pogledaj sta je ovaj MongoRepository
@Repository
public interface IRoleRepository extends JpaRepository<Role, UUID> {
}
