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
    @Column( columnDefinition = "json" )
    private String specifications = "{}";
    private Double price = 0.00;
    private String picture = "";
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

    @OneToMany(mappedBy = "product")
    private Set<OrderProduct> orders = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<WishlistProduct> wishlists = new HashSet<>();

    public boolean isEnabled() {
        return !isDisabled;
    }

    public boolean isCorrectBrand(Brand brand) {
        return this.brand.equals(brand) && isEnabled();
    }

    public boolean isCorrectCategory(Category category) {
        return this.category.equals(category) && isEnabled();
    }
}
