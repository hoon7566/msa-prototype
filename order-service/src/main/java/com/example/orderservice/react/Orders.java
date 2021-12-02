package com.example.orderservice.react;


import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class Orders {

    @Id
    private Long orderId;
    private Long productId;
    private Integer qty;
    private String userId;
    private LocalDateTime orderTime;
    private Integer totalPrice;
    private String paymentYn;
    private String deliveryYn;



}

