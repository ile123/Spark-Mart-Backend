package com.ilario.sparkmart.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name = "";
    private String description = "";
    private String shortDescription = "";
    private String picture = "";
    @Column( columnDefinition = "json" )
    private String specifications = "{}";
    private Double price = 0.00;
    private Integer quantity = 0;

    private Boolean isDisabled = false;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    private Set<OrderProduct> orders = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    private Set<WishlistProduct> wishlists = new HashSet<>();

    public boolean isEnabled() {
        return !isDisabled;
    }

}
