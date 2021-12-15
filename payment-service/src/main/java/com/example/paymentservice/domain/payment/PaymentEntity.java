package com.example.paymentservice.domain.payment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    private Long paymentId;

    @Column(nullable = false , length = 50)
    private Long orderId;

    @Column(nullable = false , length = 50)
    private Long price;

    @Column(nullable = false , length = 50)
    private String errorYn;

    @Column(nullable = false , length = 50)
    private LocalDateTime createdAt;


}

