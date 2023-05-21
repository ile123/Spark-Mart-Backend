package com.ilario.sparkmart.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;

//maybe change to ManyToMany?
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String streetAddress;
    private String city;
    private String postalCode;
    private String province;
    private String country;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy="address")
    private Set<User> users = new HashSet<>();
}