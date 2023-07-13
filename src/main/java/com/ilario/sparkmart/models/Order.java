package com.ilario.sparkmart.models;

import com.ilario.sparkmart.security.misc.enums.OrderStatus;
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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String orderNO = "";
    private LocalDateTime orderDate = LocalDateTime.now();
    private Double total = 0.00;
    private LocalDateTime shippingDate = LocalDateTime.now().plusDays(1);
    private Boolean isComplete = false;
    private LocalDateTime arrivalDate = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy="order")
    private Set<OrderProduct> products = new HashSet<>();
}
