package com.example.paymentservice.domain.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "order-service")
public interface PaymentServiceClient {

    @PutMapping("/order-service/{orderId}")
    List<Object> updateOrder(@PathVariable Long orderId);
}
