package com.example.paymentservice.jpa;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class PaymentEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    private Long paymentId;

    @Column(nullable = false , length = 50)
    private Long orderId;

    @Column(nullable = false , length = 50)
    private Integer price;

    @Column(nullable = false , length = 50)
    private String errorYn;

    @Column(nullable = false , length = 50)
    private LocalDateTime createdAt;


}

