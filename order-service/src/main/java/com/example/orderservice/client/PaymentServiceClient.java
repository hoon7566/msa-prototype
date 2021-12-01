package com.example.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "payment-service")
public interface PaymentServiceClient {

    @PostMapping("/payment-service/{orderId}")
    List<Object> createPayment(@PathVariable Long orderId);
}
