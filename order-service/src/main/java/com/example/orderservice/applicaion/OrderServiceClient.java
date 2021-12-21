package com.example.orderservice.applicaion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "catalog-service" , url = "http://localhost:8000/")
public interface OrderServiceClient {

    @GetMapping("/catalog-service/productStock/{productId}")
    Integer getProductStock(@PathVariable Long productId);

    @PutMapping("/catalog-service/productStock/{productId}")
    Integer putProductStock(@PathVariable Long productId);
}
