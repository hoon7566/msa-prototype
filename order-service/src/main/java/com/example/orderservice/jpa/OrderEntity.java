package com.example.orderservice.jpa;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    private Long orderId;

    @Column(nullable = false , length = 50)
    private Long productId;

    @Column(nullable = false , length = 50)
    private Integer qty;

    @Column(nullable = false , length = 50)
    private String userId;

    @Column(nullable = false , length = 50)
    private LocalDateTime orderTime;

    @Column(nullable = false , length = 50)
    private Integer totalPrice;

    @Column(nullable = false , length = 50)
    private String paymentYn;

    @Column(nullable = false , length = 50)
    private String deliveryYn;



}

