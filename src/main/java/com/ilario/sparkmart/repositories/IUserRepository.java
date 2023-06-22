package com.ilario.sparkmart.repositories;

import com.ilario.sparkmart.models.User;

import com.ilario.sparkmart.security.misc.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT x FROM User x WHERE x.role = :role")
    Page<User> findAllByRole(@Param("role") Role role, Pageable pageable);

    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(x) > 0 FROM User x WHERE x.role= 'ADMINISTRATOR'")
    Boolean doesAdministratorExist();

    @Query("SELECT x FROM User x WHERE (LOWER(x.firstName) LIKE %:keyword% OR LOWER(x.lastName) LIKE %:keyword% OR LOWER(x.email) LIKE %:keyword%) AND x.role = :role")
    public Page<User> findAllByKeyword(@Param("role") Role role, @Param("keyword") String keyword, Pageable pageable);
}
