package com.example.orderservice.applicaion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "catalog-service" , url = "http://localhost:8000/")
public interface OrderServiceClient {

    @GetMapping("/catalog-service/productStock/{productId}")
    Integer productStock(@PathVariable Long productId);
}
